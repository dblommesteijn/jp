package net.thepinguin.jp.external;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.cmd.ICommand;
import net.thepinguin.jp.external.abst.AbstractCommandTest;
import net.thepinguin.jp.helper.OutputFilter;

public class GenericCmdTest extends AbstractCommandTest {

	private OutputFilter _of;
	private File _testRepos;
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 * @throws Exception 
	 */
	public GenericCmdTest(String testName) throws Exception {
		super(testName);
		_of = super.getOutputFilter();
		_testRepos = super.getTestRepos();
	}
	
	/**
	 * Test empty option without arguments
	 */
	public void testEmptyOptionWithoutArguments() {
		try {
			String[] argv = new String[] { "/some/path/to/repo/" };
			_of.capture();
			App.main(argv);
			
		} catch (ExitException e) {
			String out = _of.getStdOutStr();
			String err = _of.getStdErrStr();
			_of.release();
			Assert.assertEquals(e.status, 1);
			Assert.assertEquals("jp: command/ options missing (try -h)\n", out);
			Assert.assertEquals("", err);
		} finally {
			_of.release();
		}
	}
	
	/**
	 * Test wrong option without parameters
	 */
	public void testWrongOptionWithoutParameters() {
		String target = _testRepos.getAbsolutePath();
		String[] cmds = new String[]{ "invalid", "invalid2", "1", "12", "invalid invalid" };
		for(String cmd : cmds){
			try {
				String[] argv = new String[] { target, cmd };
				_of.capture();
				App.main(argv);
				_of.release();
			} catch (ExitException e) {
				// no forced exit!
				String out = _of.getStdOutStr();
				_of.release();
				StringBuilder sb = new StringBuilder();
				sb.append("jp: invalid command/ option: ").append(cmd).append(" (try -h)").append(App.EOL);
				Assert.assertEquals(sb.toString(), out);
				Assert.assertEquals(1, e.status);
			} finally {
				_of.release();
			}
		}
	}
	
 
}
