package net.thepinguin.jp.external;

import java.io.File;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.external.abst.AbstractCommandTest;
import net.thepinguin.jp.external.abst.CallDelta;
import net.thepinguin.jp.helper.OutputFilter;

public class VersionTest extends AbstractCommandTest {

	
	public static Test suite() {
		return new TestSuite(VersionTest.class);
	}
	
	public VersionTest(String testName) {
		super(testName);
	}
	
	public void testOptionWithArgumentVersion() {
		String[] argv = new String[] { "/some/path/to/repo/", "-V" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		String out = cd.getStdOutStr();
		String err = cd.getStdErrStr();
		Assert.assertEquals(out, "jp: version: 0.2.1\n");
		Assert.assertEquals(err, "");
		Assert.assertEquals(cd.getStatus(), 0);
	}
	
	public void testOptionWithoutArgumentVersion() {
		String[] argv = new String[] { "/some/path/to/repo/", "version" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		String out = cd.getStdOutStr();
		String err = cd.getStdErrStr();
		Assert.assertEquals("jp: version: 0.2.1\n", out);
		Assert.assertEquals("", err);
		Assert.assertEquals(cd.getStatus(), 0);
	}
	
	public void testOptionVersionWithArgumentVersion() {
		String[] argv = new String[] { "/some/path/to/repo/", "version", "-V" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		String out = cd.getStdOutStr();
		String err = cd.getStdErrStr();
		Assert.assertEquals(out, "jp: version: 0.2.1\n");
		Assert.assertEquals(err, "");
		Assert.assertEquals(cd.getStatus(), 0);
	}
	
	public void testOptionVerboseArgumentVersion() {
		String[] argv = new String[] { "/some/path/to/repo/", "version", "-v" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		String out = cd.getStdOutStr();
		String err = cd.getStdErrStr();
		String b = App.getBuild();
		StringBuilder sb = new StringBuilder();
		sb.append(" ### VERBOSE output").append(App.EOL);
		sb.append(" ... build: " + b).append(App.EOL);
		sb.append("jp: version: 0.2.1").append(App.EOL);
		sb.append(" ### handling cmd found: version").append(App.EOL);
		Assert.assertEquals(sb.toString(), out);
		Assert.assertEquals(err, "");
		Assert.assertEquals(cd.getStatus(), 0);
	}

}
