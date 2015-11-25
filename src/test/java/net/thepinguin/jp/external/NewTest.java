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
import net.thepinguin.jp.helper.OutputFilter;

public class NewTest extends AbstractCommandTest {

//	private OutputFilter _of;
	private File _testRepos;
	
	public static Test suite() {
		return new TestSuite(NewTest.class);
	}
	
	public NewTest(String testName) {
		super(testName);
//		_of = super.getOutputFilter();
		_testRepos = super.getTestRepos();
	}
	
	public void testWithoutArtifactId() throws IOException {
		long now = System.currentTimeMillis();
		String target = _testRepos.getAbsolutePath();
		String artifactId = "testoptionnewwithoutartifactid" + now;
		String groupId = "net.thepinguin.jp." + artifactId;
		File testOptionNew = new File(target, artifactId);
		
		String[] argv = new String[] { target, "new",  groupId };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		String out = cd.getStdOutStr();
		String err = cd.getStdErrStr();
		StringBuilder sb = new StringBuilder();
		sb.append("... project created: testoptionnewwithoutartifactid" + now).append(App.EOL);
		sb.append("... collecting deps for: testoptionnewwithoutartifactid" +now).append(App.EOL);
		sb.append("jp: collecting...").append(App.EOL);
		sb.append("  junit (buildin).. OK").append(App.EOL);
		sb.append("jp: finished").append(App.EOL);
		Assert.assertEquals(sb.toString(), out);
		Assert.assertEquals("", err);
		// test newly created project
		Assert.assertTrue(testOptionNew.exists());
		Assert.assertTrue(testOptionNew.isDirectory());
		FileUtils.deleteDirectory(testOptionNew);
		

	}
	
	public void testWithArtifactId() throws IOException {
		long now = System.currentTimeMillis();
		String target = _testRepos.getAbsolutePath();
		String groupId = "net.thepinguin.jp.testoptionnewwithartifactid";
		String artifactId = "testaltartifactid" + now;
		File testOptionNew = new File(target, artifactId);
		String[] argv = new String[] { target, "new",  groupId, artifactId };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		String out = cd.getStdOutStr();
		String err = cd.getStdErrStr();
		StringBuilder sb = new StringBuilder();
		sb.append("... project created: testaltartifactid" + now).append(App.EOL);
		sb.append("... collecting deps for: testaltartifactid" +now).append(App.EOL);
		sb.append("jp: collecting...").append(App.EOL);
		sb.append("  junit (buildin).. OK").append(App.EOL);
		sb.append("jp: finished").append(App.EOL);
		Assert.assertEquals(sb.toString(), out);
		Assert.assertEquals("", err);		
		// test newly created project
		Assert.assertTrue(testOptionNew.exists());
		Assert.assertTrue(testOptionNew.isDirectory());
		FileUtils.deleteDirectory(testOptionNew);
	}
	
	public void testWithoutParameters() {
		String target = _testRepos.getAbsolutePath();
		String[] argv = new String[] { target, "new" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		String out = cd.getStdOutStr();
		String err = cd.getStdErrStr();
		// main should exit with != 0
		Assert.assertEquals(1, cd.getStatus());
		// main should echo missing argument statement
		Assert.assertEquals("jp: new: missing argument (try -h)\n", out);
		Assert.assertEquals("", err);
	}

	
	
}
