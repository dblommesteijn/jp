package net.thepinguin.jp.external;

import java.io.File;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.external.AbstractCommandTest.ExitException;
import net.thepinguin.jp.helper.OutputFilter;

public class InitTest extends AbstractCommandTest {

	private OutputFilter _of;
	private File _testRepos;
	
	public static Test suite() {
		return new TestSuite(InitTest.class);
	}
	
	public InitTest(String testName) {
		super(testName);
		_of = super.getOutputFilter();
		_testRepos = super.getTestRepos();
	}
	
	public void testOptionInitIncorrectPath() {
		String target = _testRepos.getAbsolutePath();
		try {
			String[] argv = new String[] { target, "init" };
			_of.capture();
			App.main(argv);
			// not reaching here!
			Assert.assertTrue(false);
		} catch (ExitException e) {
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			_of.release();
			StringBuilder sb = new StringBuilder();
			sb.append("jp: pom.xml not found (try -h)").append(App.EOL);
			Assert.assertEquals(sb.toString(), out);
			Assert.assertEquals("", err);
			Assert.assertEquals(1, e.status);
		} finally {
			_of.release();
		}
	}

}
