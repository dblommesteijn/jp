package net.thepinguin.jp.external;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.helper.OutputFilter;

public class NewTest extends AbstractCommandTest {

	private OutputFilter _of;
	private File _testRepos;
	
	public static Test suite() {
		return new TestSuite(NewTest.class);
	}
	
	public NewTest(String testName) {
		super(testName);
		_of = super.getOutputFilter();
		_testRepos = super.getTestRepos();
	}
	
	public void testWithoutArtifactId() {
		long now = System.currentTimeMillis();
		String target = _testRepos.getAbsolutePath();
		String artifactId = "testoptionnewwithoutartifactid" + now;
		String groupId = "net.thepinguin.jp." + artifactId;
		File testOptionNew = new File(target, artifactId);
		try {
			String[] argv = new String[] { target, "new",  groupId };
			_of.capture();
			App.main(argv);
			_of.release();
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
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
			
		} catch (ExitException e) {
			// main cannot throw exit -1
			Assert.assertEquals("", e.getMessage());
		} catch (IOException e) {
			// cannot throw away test repo dir
			Assert.assertEquals("", e.getMessage());
		} finally {
			_of.release();
		}
	}
	
	public void testWithArtifactId() {
		long now = System.currentTimeMillis();
		String target = _testRepos.getAbsolutePath();
		String groupId = "net.thepinguin.jp.testoptionnewwithartifactid";
		String artifactId = "testaltartifactid" + now;
		File testOptionNew = new File(target, artifactId);
		try {
			String[] argv = new String[] { target, "new",  groupId, artifactId };
			_of.capture();
			App.main(argv);
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			_of.release();
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
		} catch (ExitException e) {
			// main cannot exit with -1
			Assert.assertEquals("", e.getMessage());
		} catch (IOException e) {
			// cannot throw away test repo dir
			Assert.assertEquals("", e.getMessage());
		} finally {
			_of.release();
		}
	}
	
	public void testWithoutParameters() {
		String target = _testRepos.getAbsolutePath();
		try {
			String[] argv = new String[] { target, "new" };
			_of.capture();
			App.main(argv);
			Assert.assertTrue(false);
			// test newly created project
		} catch (ExitException e) {
			_of.release();
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			
			// main should exit with != 0
			Assert.assertEquals(1, e.status);
			// main should echo missing argument statement
			Assert.assertEquals("jp: new: missing argument (try -h)\n", out);
			Assert.assertEquals("", err);
		} finally {
			_of.release();
		}
	}

	
	
}
