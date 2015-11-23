package net.thepinguin.jp.internal;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import edu.emory.mathcs.backport.java.util.Collections;
import junit.framework.Assert;
import junit.framework.TestCase;
import net.thepinguin.jp.App;
import net.thepinguin.jp.cmd.ICommand;

/**
 * These tests determine the accessibility of Commands
 */
public class CommandLoadingTest extends TestCase {

	/**
	 * These commands should exist to execute
	 */
	public void testAvailableCommands(){
		String[] cmds = { "collect", "help", "init", "new", "repo", "status", "verbose", "version" };
		for(String cmd : cmds){
			// all
			ICommand c1 = App.getAllCommands().get(cmd);
			Assert.assertNotNull(c1);
			Assert.assertEquals(c1.getId(), cmd);
			// callable
			ICommand c2 = App.getCallableCommands().get(cmd);
			if(c2 == null){
				Assert.assertFalse(c1.isCallable());
			}
		}
	}
	
	
	/**
	 * These commands should not exist to execute
	 */
	public void testUnvailableCommands(){
		String[] cmds = { "me", "us", "you", "123", "12.21", "n.a.b.c" };
		for(String cmd : cmds){
			ICommand c1 = App.getAllCommands().get(cmd);
			Assert.assertNull(c1);
			ICommand c2 = App.getCallableCommands().get(cmd);
			Assert.assertNull(c2);
		}
	}
	
	/**
	 * These commands should be available from outside
	 */
	public void testAvailableCommandsCallable() {
		String[] cmds = { "collect", "help", "init", "new", "status", "verbose", "version" };
		for(String cmd : cmds){
			ICommand c = App.getAllCommands().get(cmd);
			Assert.assertNotNull(c);
			Assert.assertEquals(c.getId(), cmd);
			Assert.assertTrue(c.isCallable());
		}
		Set<String> callable = App.getCallableCommands().keySet();
		Set<String> all = App.getAllCommands().keySet();
		// callable commands should be a subset of all
		Assert.assertTrue(all.containsAll(callable));
	}
	
	/**
	 * These commands should only be available inside
	 */
	public void testAvailableCommandsNotCallable() {
		String[] cmds = { "repo" };
		for(String cmd : cmds){
			// should exist as command
			ICommand c1 = App.getAllCommands().get(cmd);
			Assert.assertNotNull(c1);
			Assert.assertEquals(c1.getId(), cmd);
			Assert.assertFalse(c1.isCallable());
			// should not exist as callable command
			ICommand c2 = App.getCallableCommands().get(cmd);
			Assert.assertNull(c2);
		}
	}
}
