package net.thepinguin.jp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Permission;

import org.apache.commons.io.FileUtils;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestCommands extends TestCase {
	
	private File _testRepos;
	
	public static boolean VERBOSE = false;
	public static boolean TRAVIS = false;
	
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public TestCommands(String testName) {
		super(testName);
		_testRepos = new File(Common.JP_HOME, "test/repos/").getAbsoluteFile();
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
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestCommands.class);
	}

	private PrintStream _stdout;
	private PrintStream _stderr;

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
			disableOutput();
			App.main(argv);
			enableOutput();
		} catch (ExitException e) {
			Assert.assertEquals(e.status, 1);
		}
	}

	public void testEmptyOptionWithArgumentVerbose() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "-v" };
			disableOutput();
			App.main(argv);
			enableOutput();
		} catch (ExitException e) {
			Assert.assertEquals(e.status, 1);
		}
	}

	public void testEmptyOptionWithArgumentHelp() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "-h" };
			disableOutput();
			App.main(argv);
			enableOutput();
			Assert.assertTrue(true);
		} catch (ExitException e) {
			Assert.assertTrue(false);
		}
	}

	public void testOptionWithoutArgumentHelp() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "help" };
			disableOutput();
			App.main(argv);
			enableOutput();
			Assert.assertTrue(true);
		} catch (ExitException e) {
			Assert.assertTrue(false);
		}
	}
	
	public void testEmptyOptionWithArgumentVersion() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "-V" };
			disableOutput();
			App.main(argv);
			enableOutput();
			Assert.assertTrue(true);
		} catch (ExitException e) {
			Assert.assertTrue(false);
		}
	}
	
	public void testEmptyOptionWithoutArgumentVersion() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "version" };
			disableOutput();
			App.main(argv);
			enableOutput();
			Assert.assertTrue(true);
		} catch (ExitException e) {
			Assert.assertTrue(false);
		}
	}


	// TODO: add option specific tests (new, collect)
	
	public void testOptionNewWithoutArtifactId() {
		
		long now = System.currentTimeMillis();
		String target = _testRepos.getAbsolutePath();
		String artifactId = "testoptionnewwithoutartifactid" + now;
		String groupId = "net.thepinguin.jp." + artifactId;
		File testOptionNew = new File(target, artifactId);
		try {
			String[] argv = new String[] { target, "new",  groupId};
			disableOutput();
			App.main(argv);
			enableOutput();
			// test newly created project
			Assert.assertTrue(testOptionNew.exists());
			Assert.assertTrue(testOptionNew.isDirectory());
			FileUtils.deleteDirectory(testOptionNew);
		} catch (ExitException e) {
			enableOutput();
			// main cannot throw exit -1
			if(TRAVIS){
				System.out.println("------------- TRAVIS");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			Assert.assertTrue(false);
		} catch (IOException e) {
			enableOutput();
			// cannot throw away test repo dir
			if(TRAVIS){
				System.out.println("------------- TRAVIS");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			Assert.assertTrue(false);
		}
	}
	
	public void testOptionNewWithArtifactId() {
		long now = System.currentTimeMillis();
		String target = _testRepos.getAbsolutePath();
		String groupId = "net.thepinguin.jp.testoptionnewwithartifactid";
		String artifactId = "testaltartifactid" + now;
		File testOptionNew = new File(target, artifactId);
		try {
			String[] argv = new String[] { target, "new",  groupId, artifactId};
			disableOutput();
			App.main(argv);
			enableOutput();
			// test newly created project
			Assert.assertTrue(testOptionNew.exists());
			Assert.assertTrue(testOptionNew.isDirectory());
			FileUtils.deleteDirectory(testOptionNew);
		} catch (ExitException e) {
			enableOutput();
			// main cannot exit with -1
			if(TRAVIS){
				System.out.println("------------- TRAVIS");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			Assert.assertTrue(false);
		} catch (IOException e) {
			enableOutput();
			// cannot throw away test repo dir
			if(TRAVIS){
				System.out.println("------------- TRAVIS");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			Assert.assertTrue(false);
		}
	}
	
	private void disableOutput() {
		// NOTE: not sure if this is working properly...
		// disable stdout stderr (App.main is echoing)
		if(!VERBOSE){
			_stdout = System.out;
			System.setOut(new PrintStream(new ByteArrayOutputStream()));
			_stderr = System.err;
			System.setErr(new PrintStream(new ByteArrayOutputStream()));
		} else {
		}
	}

	private void enableOutput() {
		if(!VERBOSE){
			// re-enable stdout, stderr
			System.setOut(_stdout);
			System.setErr(_stderr);
		}
	}

}
