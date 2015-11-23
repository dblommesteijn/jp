package net.thepinguin.jp.external;

import java.io.File;
import java.security.Permission;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.thepinguin.jp.App;
import net.thepinguin.jp.helper.OutputFilter;

/**
 * Abstract Command Test helper
 *  - Generalizes CommandTests;
 *  - Captures exit status and throws ExitException accordingly
 */
public abstract class AbstractCommandTest extends TestCase {
	private File _testRepos;
	/**
	 * Path to test repo directory 
	 * @return file to test repo directory
	 */
	public File getTestRepos(){
		return _testRepos;
	}
	
	private OutputFilter _of;
	/**
	 * Get instance of output filtering (stderr/ stdout)
	 * @return instance of output filter
	 */
	public OutputFilter getOutputFilter() {
		return _of;
	}
	
	public static boolean VERBOSE = false;
	public static boolean TRAVIS = false;
	
	/**
	 * Exit Exception, thrown on exit with statuscode
	 */
	protected static class ExitException extends SecurityException {
		private static final long serialVersionUID = 2338670599298955850L;
		public final int status;
		
		/**
		 * Exit constructor, exit always has an exception status
		 * @param status exit status
		 */
		public ExitException(int status) {
			super();
			this.status = status;
		}
	}

	private static class NoExitSecurityManager extends SecurityManager {
		/**
		 * Suppress check permission
		 */
		public void checkPermission(Permission perm) {
		}

		/**
		 * Suppress check permission
		 */
		public void checkPermission(Permission perm, Object context) {
		}

		/**
		 * On exit throw ExitException with exit status
		 */
		public void checkExit(int status) {
			super.checkExit(status);
			throw new ExitException(status);
		}
	}
	
	public AbstractCommandTest(String testName) {
		super(testName);
		_testRepos = new File(App.JP_HOME, "test/repos/").getAbsoluteFile();
		if(!_testRepos.exists())
			_testRepos.mkdirs();
		if(!_testRepos.isDirectory())
			Assert.assertFalse(true);
		String s = System.getenv("JP_VERBOSE");
		if(s != null)
			VERBOSE = (s.equals("true"));
		String t = System.getenv("JP_TRAVIS");
		if(t != null)
			TRAVIS = (t.equals("true"));
		
		_of = new OutputFilter(); 
	}

	public void setUp() throws Exception {
		super.setUp();
		System.setSecurityManager(new NoExitSecurityManager());
	}

	public void tearDown() throws Exception {
		super.tearDown();
		System.setSecurityManager(null); // or save and restore original
	}
	
	
}
