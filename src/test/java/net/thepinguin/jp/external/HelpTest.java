package net.thepinguin.jp.external;


import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.helper.OutputFilter;

public class HelpTest extends AbstractCommandTest {

	private OutputFilter _of;
	
	public static Test suite() {
		return new TestSuite(HelpTest.class);
	}
	
	public HelpTest(String testName) {
		super(testName);
		_of = super.getOutputFilter();
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
	
	public void testOptionHelpWithArgumentHelp() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/", "help", "-h" };
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
}
