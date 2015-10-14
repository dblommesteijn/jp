package net.thepinguin.jp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Permission;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestCommands extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public TestCommands(String testName) {
		super(testName);
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
		// disable stdout stderr (App.main is echoing)
		_stdout = System.out;
		System.setOut(new PrintStream(new ByteArrayOutputStream()));
		_stderr = System.err;
		System.setErr(new PrintStream(new ByteArrayOutputStream()));
		System.setSecurityManager(new NoExitSecurityManager());
	}

	public void tearDown() throws Exception {
		System.setSecurityManager(null); // or save and restore original
		// re-enable stdout, stderr
		System.setOut(_stdout);
		System.setErr(_stderr);
		super.tearDown();
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
			App.main(argv);
		} catch (ExitException e) {
			Assert.assertEquals(e.status, 1);
		}
	}

	public void testEmptyOptionWithArgumentVerbose() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "-v" };
			App.main(argv);
		} catch (ExitException e) {
			Assert.assertEquals(e.status, 1);
		}
	}

	public void testEmptyOptionWithArgumentHelp() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "-h" };
			App.main(argv);
			Assert.assertTrue(true);
		} catch (ExitException e) {
			Assert.assertTrue(false);
		}
	}

	public void testOptionWithoutArgumentHelp() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "help" };
			App.main(argv);
			Assert.assertTrue(true);
		} catch (ExitException e) {
			Assert.assertTrue(false);
		}
	}

	// TODO: add option specific tests (new, collect)

}
