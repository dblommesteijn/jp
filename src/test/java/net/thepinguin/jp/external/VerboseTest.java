package net.thepinguin.jp.external;

import java.io.File;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.external.abst.AbstractCommandTest;
import net.thepinguin.jp.external.abst.CallDelta;
import net.thepinguin.jp.helper.OutputFilter;

public class VerboseTest extends AbstractCommandTest {

	private OutputFilter _of;
	
	public static Test suite() {
		return new TestSuite(VerboseTest.class);
	}
	
	public VerboseTest(String testName) throws Exception {
		super(testName);
		_of = super.getOutputFilter();
	}
	
	public void testWithArgumentVerbose() {
		String[] argv = new String[] { "/some/path/to/repo/", "-v" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		String out = cd.getStdOutStr();
		String err = cd.getStdErrStr();
		Assert.assertEquals(1, cd.getStatus());
		StringBuilder sb = new StringBuilder();
		sb.append(" ### VERBOSE output").append(App.EOL);
		sb.append(" ### -> ").append(App.EOL);
		sb.append("`command/ options missing`").append(App.EOL);
		sb.append(" ### <- ").append(App.EOL);
		sb.append("jp: command/ options missing (try -h)").append(App.EOL);
		Assert.assertEquals(sb.toString(), out);
		Assert.assertTrue(err.startsWith("java.lang.Exception: command/ options missing"));
	}
	
}
