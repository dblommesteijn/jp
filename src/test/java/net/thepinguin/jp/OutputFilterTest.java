package net.thepinguin.jp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Permission;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.thepinguin.jp.helper.OutputFilter;

public class OutputFilterTest extends TestCase {
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OutputFilterTest( String testName )
    {
        super( testName );
    }

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(OutputFilterTest.class);
	}

	private PrintStream _stdout;
	private ByteArrayOutputStream _newByteOut;
	private PrintStream _stderr;
	private ByteArrayOutputStream _newByteErr;

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
	
	public void testEchoing() throws IOException {
		OutputFilter of = new OutputFilter();
		// start capture
		of.capture();
		
		// print to out
		System.out.print("123");
		Assert.assertEquals(of.getStdOutStr(), "123");
		Assert.assertEquals(of.getStdOutStr(), "");
		// print to err
		System.err.print("456");
		Assert.assertEquals(of.getStdErrStr(), "456");
		Assert.assertEquals(of.getStdErrStr(), "");
		
		// reload resources
		System.out.print("abc");
		System.err.print("abc");
		of.clear();
		Assert.assertEquals(of.getStdOutStr(), "");
		Assert.assertEquals(of.getStdErrStr(), "");
		of.release();
		
		// reuse same object
		of.capture();
		System.out.print("789");
		Assert.assertEquals(of.getStdOutStr(), "789");
		Assert.assertEquals(of.getStdOutStr(), "");
		System.err.print("012");
		Assert.assertEquals(of.getStdErrStr(), "012");
		Assert.assertEquals(of.getStdErrStr(), "");
		of.release();
		
		// try incorrect use of capture/ release
		of.release();
		of.capture();
		System.out.print("def");
		System.err.print("def");
		Assert.assertEquals(of.getStdOutStr(), "def");
		Assert.assertEquals(of.getStdOutStr(), "");
		Assert.assertEquals(of.getStdErrStr(), "def");
		Assert.assertEquals(of.getStdErrStr(), "");
		of.release();
		
		of.capture();
		of.capture();
		System.out.print("ghi");
		System.err.print("ghi");
		Assert.assertEquals(of.getStdOutStr(), "ghi");
		Assert.assertEquals(of.getStdOutStr(), "");
		Assert.assertEquals(of.getStdErrStr(), "ghi");
		Assert.assertEquals(of.getStdErrStr(), "");
		of.release();
	}
}
