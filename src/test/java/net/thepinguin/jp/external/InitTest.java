package net.thepinguin.jp.external;

import java.io.File;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.external.abst.AbstractCommandTest;
import net.thepinguin.jp.external.abst.CallDelta;

public class InitTest extends AbstractCommandTest {

	private File _testRepos;
	
	public static Test suite() {
		return new TestSuite(InitTest.class);
	}
	
	public InitTest(String testName) {
		super(testName);
		_testRepos = super.getTestRepos();
	}
	
	public void testOptionInitIncorrectPath() {
		String target = _testRepos.getAbsolutePath();
		String[] argv = new String[] { target, "init" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		StringBuilder sb = new StringBuilder();
		sb.append("jp: pom.xml not found (try -h)").append(App.EOL);
		Assert.assertEquals(sb.toString(), cd.getStdOutStr());
		Assert.assertEquals("", cd.getStdErrStr());
		Assert.assertEquals(1, cd.getStatus());		
	}

}
