package net.thepinguin.jp.external;

import java.io.File;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.helper.OutputFilter;

public class VersionTest extends AbstractCommandTest {

	private OutputFilter _of;
	
	public static Test suite() {
		return new TestSuite(VersionTest.class);
	}
	
	public VersionTest(String testName) {
		super(testName);
		_of = super.getOutputFilter();
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

}
