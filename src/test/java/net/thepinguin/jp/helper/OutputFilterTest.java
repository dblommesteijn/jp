package net.thepinguin.jp.helper;

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
	
	public void testStdOutEchoing() throws IOException {
		OutputFilter of = new OutputFilter();
		// start capture
		of.capture();
		System.out.print("abc");
		Assert.assertEquals(of.getStdOutStr(), "abc");
		Assert.assertEquals(of.getStdOutStr(), "");
		// stop capture (release)
		of.release();
	}
	
	public void testStdErrEchoing() throws IOException {
		OutputFilter of = new OutputFilter();
		// start capture
		of.capture();
		// print to err
		System.err.print("123");
		Assert.assertEquals(of.getStdErrStr(), "123");
		Assert.assertEquals(of.getStdErrStr(), "");
		// stop capture (release)
		of.release();
	}
	
	public void testClearAndEchoing() throws IOException {
		OutputFilter of = new OutputFilter();
		// start capture
		of.capture();
		System.out.print("abc");
		System.err.print("123");
		of.clear();
		Assert.assertEquals(of.getStdOutStr(), "");
		Assert.assertEquals(of.getStdErrStr(), "");
		of.release();
	}
	
	public void testObjectReuse() throws IOException {
		OutputFilter of = new OutputFilter();
		of.capture();
		System.out.print("abc");
		Assert.assertEquals(of.getStdOutStr(), "abc");
		Assert.assertEquals(of.getStdOutStr(), "");
		of.release();
		of.capture();
		System.out.print("abc");
		Assert.assertEquals(of.getStdOutStr(), "abc");
		Assert.assertEquals(of.getStdOutStr(), "");
		of.release();
	}
	
	public void testMultiCapture() throws IOException {
		OutputFilter of = new OutputFilter();
		of.capture();
		of.capture();
		System.out.print("abc");
		Assert.assertEquals(of.getStdOutStr(), "abc");
		Assert.assertEquals(of.getStdOutStr(), "");
		// double release
		of.release();
		of.release();
		// double capture
		of.capture();
		of.capture();
		System.out.print("abc");
		Assert.assertEquals(of.getStdOutStr(), "abc");
		Assert.assertEquals(of.getStdOutStr(), "");
		of.release();
	}
	
	public void testMultiObject() throws IOException {
		OutputFilter of = new OutputFilter();
		of.capture();
		System.out.print("abc");
		Assert.assertEquals(of.getStdOutStr(), "abc");
		Assert.assertEquals(of.getStdOutStr(), "");
		of.release();
		// overwrite of with new instance
		of = new OutputFilter();
		of.capture();
		System.out.print("abc");
		Assert.assertEquals(of.getStdOutStr(), "abc");
		Assert.assertEquals(of.getStdOutStr(), "");
		of.release();
	}
	

}
