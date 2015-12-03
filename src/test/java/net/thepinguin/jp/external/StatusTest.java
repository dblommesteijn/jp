package net.thepinguin.jp.external;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.external.abst.AbstractCommandTest;
import net.thepinguin.jp.external.abst.CallDelta;

public class StatusTest extends AbstractCommandTest {
	
	private File _testRepos;
	
	public static Test suite() {
		return new TestSuite(StatusTest.class);
	}
	
	public StatusTest(String testName) throws Exception {
		super(testName);
		_testRepos = super.getTestRepos();
	}
	
	public void testNewProjectStatus() throws IOException {
		String target = _testRepos.getAbsolutePath();
		String artifactId = "testnewprojectstatus";
		String groupId = "net.thepinguin.jp." + artifactId;
		String version = "1.0-SNAPSHOT";
		File testOptionNew = new File(target, artifactId);
		// create new project
		String[] argv = new String[] { target, "new",  groupId };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		Assert.assertEquals(0, cd.getStatus());
		// invoke jp status
		argv = new String[] { testOptionNew.getAbsolutePath(), "status", };
		cd = AbstractCommandTest.callMain(argv);
		Assert.assertEquals(0, cd.getStatus());
		String out = cd.getStdOutStr();
		StringBuilder sb = new StringBuilder();
		sb.append("jp: project: `").append(artifactId).append("` (").append(version).append(")").append(App.EOL);
		sb.append(" ... groupId: ").append(groupId).append(App.EOL);
		Assert.assertEquals(sb.toString(), out);
		// cleanup
		FileUtils.deleteDirectory(testOptionNew);
	}
	
}
