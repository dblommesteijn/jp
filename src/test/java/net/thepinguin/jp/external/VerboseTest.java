package net.thepinguin.jp.external;

import java.io.File;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.helper.OutputFilter;

public class VerboseTest extends AbstractCommandTest {

	private OutputFilter _of;
	
	public static Test suite() {
		return new TestSuite(VerboseTest.class);
	}
	
	public VerboseTest(String testName) {
		super(testName);
		_of = super.getOutputFilter();
	}
	
	public void testWithArgumentVerbose() {
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
	
}
