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

public class InitTest extends AbstractCommandTest {

	private File _testRepos;
	
	public static Test suite() {
		return new TestSuite(InitTest.class);
	}
	
	public InitTest(String testName) throws Exception {
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
	
	public void testOptionInitCorrectPath() throws IOException {
		String target = _testRepos.getAbsolutePath();
		// create new project
		String groupId = "net.thepinguin.jp";
		String artifactId = "testoptioninitcorrectpath";
		String[] argv = new String[] { target, "new", groupId, artifactId };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		Assert.assertEquals(0, cd.getStatus());
		// remove JPacker file (de-init jp)
		File newProject = new File(target, artifactId);
		File d = new File(newProject, "JPacker");
		Assert.assertTrue(d.exists());
		Assert.assertTrue(d.delete());
		Assert.assertFalse(d.exists());
		// init jp
		argv = new String[] { newProject.getAbsolutePath(), "init" };
		cd = AbstractCommandTest.callMain(argv);
		System.out.println("-+---------------------");
		System.out.println(cd.getStdOutStr());
		System.out.println("-+---------------------");
		System.out.println(cd.getStdErrStr());
		System.out.println("-+---------------------");
		Assert.assertEquals(0, cd.getStatus());
		Assert.assertTrue(d.exists());
		FileUtils.deleteDirectory(newProject);
	}
	
	public void testOptionInitCorrectPathDoubleInit() throws IOException {
		String target = _testRepos.getAbsolutePath();
		// create new project
		String groupId = "net.thepinguin.jp";
		String artifactId = "testoptioninitcorrectpath";
		String[] argv = new String[] { target, "new", groupId, artifactId };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		Assert.assertEquals(0, cd.getStatus());
		// remove JPacker file (de-init jp)
		File newProject = new File(target, artifactId);
		File d = new File(newProject, "JPacker");
		Assert.assertTrue(d.exists());
		Assert.assertTrue(d.delete());
		Assert.assertFalse(d.exists());
		// init jp
		argv = new String[] { newProject.getAbsolutePath(), "init" };
		cd = AbstractCommandTest.callMain(argv);
		Assert.assertEquals(0, cd.getStatus());
		Assert.assertTrue(d.exists());
		// init jp again
		cd = AbstractCommandTest.callMain(argv);
		Assert.assertEquals(1, cd.getStatus());
		Assert.assertTrue(d.exists());
		StringBuilder sb = new StringBuilder();
		sb.append("jp: JPacker already present, not creating new one (try -h)").append(App.EOL);
		Assert.assertEquals(sb.toString(), cd.getStdOutStr());
		FileUtils.deleteDirectory(newProject);
	}

}
