package net.thepinguin.jp.external;


import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.thepinguin.jp.external.abst.AbstractCommandTest;
import net.thepinguin.jp.external.abst.CallDelta;

public class HelpTest extends AbstractCommandTest {
	
	public static Test suite() {
		return new TestSuite(HelpTest.class);
	}
	
	public HelpTest(String testName) throws Exception {
		super(testName);
	}

	public void testEmptyOptionWithArgumentHelp() {
		String[] argv = new String[] { "/some/path/to/repo/", "-h" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		Assert.assertTrue(cd.getStdOutStr().startsWith("Usage: jp [-options] [commands...]"));
		Assert.assertEquals("", cd.getStdErrStr());
		Assert.assertEquals(0, cd.getStatus());		
	}

	public void testOptionWithoutArgumentHelp() {
		String[] argv = new String[] { "/some/path/to/repo/", "help" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		Assert.assertTrue(cd.getStdOutStr().startsWith("Usage: jp [-options] [commands...]"));
		Assert.assertEquals("", cd.getStdErrStr());
		Assert.assertEquals(0, cd.getStatus());
	}
	
	public void testOptionHelpWithArgumentHelp() {
		String[] argv = new String[] { "/some/path/to/repo/", "help", "-h" };
		CallDelta cd = AbstractCommandTest.callMain(argv);
		Assert.assertTrue(cd.getStdOutStr().startsWith("Usage: jp [-options] [commands...]"));
		Assert.assertEquals("", cd.getStdErrStr());
		Assert.assertEquals(0, cd.getStatus());
	}
}
