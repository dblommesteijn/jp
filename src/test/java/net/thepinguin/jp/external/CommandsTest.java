package net.thepinguin.jp.external;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.cmd.ICommand;
import net.thepinguin.jp.helper.OutputFilter;

public class CommandsTest extends TestCase {
	
	private File _testRepos;
	private OutputFilter _of;
	
	public static boolean VERBOSE = false;
	public static boolean TRAVIS = false;
	
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public CommandsTest(String testName) {
		super(testName);
		_testRepos = new File(App.JP_HOME, "test/repos/").getAbsoluteFile();
		if(!_testRepos.exists())
			_testRepos.mkdirs();
		if(!_testRepos.isDirectory())
			Assert.assertFalse(true);
		String s = System.getenv("JP_VERBOSE");
		if(s != null)
			VERBOSE = (s.equals("true"));
		String t = System.getenv("JP_TRAVIS");
		if(t != null)
			TRAVIS = (t.equals("true"));
		
		_of = new OutputFilter(); 
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(CommandsTest.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		System.setSecurityManager(new NoExitSecurityManager());
	}

	public void tearDown() throws Exception {
		super.tearDown();
		System.setSecurityManager(null); // or save and restore original
	}

	protected static class ExitException extends SecurityException {
		private static final long serialVersionUID = 2338670599298955850L;
		public final int status;

		public ExitException(int status) {
			super();
			this.status = status;
		}
	}

	private static class NoExitSecurityManager extends SecurityManager {
		@Override
		public void checkPermission(Permission perm) {
		}

		@Override
		public void checkPermission(Permission perm, Object context) {
		}

		@Override
		public void checkExit(int status) {
			super.checkExit(status);
			throw new ExitException(status);
		}
	}
	


	public void testEmptyOptionWithoutArguments() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/" };
			_of.capture();
			App.main(argv);
			
		} catch (ExitException e) {
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			_of.release();
			Assert.assertEquals(e.status, 1);
			Assert.assertEquals("jp: command/ options missing (try -h)\n", out);
			Assert.assertEquals("", err);
		} finally {
			_of.release();
		}
	}

	public void testEmptyOptionWithArgumentVerbose() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "-v" };
			_of.capture();
			App.main(argv);
		} catch (ExitException e) {
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			_of.release();
			Assert.assertEquals(1, e.status);
			StringBuilder sb = new StringBuilder();
			sb.append(" ### VERBOSE output").append(App.EOL);
			sb.append(" ### -> ").append(App.EOL);
			sb.append("`command/ options missing`").append(App.EOL);
			sb.append(" ### <- ").append(App.EOL);
			sb.append("jp: command/ options missing (try -h)").append(App.EOL);
			Assert.assertEquals(sb.toString(), out);
			Assert.assertTrue(err.startsWith("java.lang.Exception: command/ options missing"));
		} finally {
			_of.release();
		}
	}

	public void testEmptyOptionWithArgumentHelp() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "-h" };
			_of.capture();
			App.main(argv);
			_of.release();
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			Assert.assertTrue(out.startsWith("Usage: jp [-options] [commands...]"));
			Assert.assertEquals("", err);
		} catch (ExitException e) {
			Assert.assertEquals("", e.getMessage());
		} finally {
			_of.release();
		}
	}

	public void testOptionWithoutArgumentHelp() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "help" };
			_of.capture();
			App.main(argv);
			_of.release();
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			Assert.assertTrue(out.startsWith("Usage: jp [-options] [commands...]"));
			Assert.assertEquals("", err);
		} catch (ExitException e) {
			Assert.assertEquals("", e.getMessage());
		} finally {
			_of.release();
		}
	}
	
	public void testEmptyOptionWithArgumentVersion() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "-V" };
			_of.capture();
			App.main(argv);
			_of.release();
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			Assert.assertEquals(out, "jp: version: 0.2.1\n");
			Assert.assertEquals("", err);
		} catch (ExitException e) {
			Assert.assertEquals("", e.getMessage());
		} finally {
			_of.release();
		}
	}
	
	public void testEmptyOptionWithoutArgumentVersion() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "version" };
			_of.capture();
			App.main(argv);
			_of.release();
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			Assert.assertEquals("jp: version: 0.2.1\n", out);
			Assert.assertEquals("", err);
		} catch (ExitException e) {
			Assert.assertEquals("", e.getMessage());
		} finally {
			_of.release();
		}
	}
	
	public void testOptionVerboseArgumentVersion() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "version", "-v" };
			_of.capture();
			App.main(argv);
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			_of.release();
			String b = App.getBuild();
			StringBuilder sb = new StringBuilder();
			sb.append(" ### VERBOSE output").append(App.EOL);
			sb.append(" ... build: " + b).append(App.EOL);
			sb.append("jp: version: 0.2.1").append(App.EOL);
			sb.append(" ### handling cmd found: version").append(App.EOL);
			Assert.assertEquals(sb.toString(), out);
			Assert.assertEquals("", err);
		} catch (ExitException e) {
			Assert.assertEquals("", e.getMessage());
			e.printStackTrace();
		} finally {
			_of.release();
		}
	}
	
	public void testOptionNewWithoutArtifactId() {
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
	
	public void testOptionNewWithArtifactId() {
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
	
	public void testOptionNewWithoutParameters() {
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
	
	public void testWrongOptionWithoutParameters() {
		String target = _testRepos.getAbsolutePath();
		String[] cmds = new String[]{ "invalid", "invalid2", "1", "12", "invalid invalid" };
		for(String cmd : cmds){
			try {
				String[] argv = new String[] { target, cmd };
				_of.capture();
				App.main(argv);
				_of.release();
			} catch (ExitException e) {
				// no forced exit!
				String out = _of.getStdOutStr();
				_of.release();
				StringBuilder sb = new StringBuilder();
				sb.append("jp: invalid command/ option: ").append(cmd).append(" (try -h)").append(App.EOL);
				Assert.assertEquals(sb.toString(), out);
				Assert.assertEquals(1, e.status);
			} finally {
				_of.release();
			}
		}
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
	
//	public void testOptionInitWithoutParameters() {
//		String target = _testRepos.getAbsolutePath();
//		String newProject = "";
//		try {
//			String[] argv = new String[] { target, "init" };
//			_of.capture();
//			App.main(argv);
//			String out = _of.getStdOutStr();
//			String err = _of.getStdErrStr();
//			_of.release();
//			System.out.println("-+------------");
//			System.out.println(out);
//			System.out.println("-+------------");
//			
//			// test newly created project
//		} catch (ExitException e) {
//			String out = _of.getStdOutStr();
//			_of.release();
//			System.out.println("-+------------");
//			System.out.println(out);
//			System.out.println("-+------------");
//			// any error here is invalid
////			Assert.assertTrue(false);
//		} finally {
//			_of.release();
//		}
//	}
 
}
