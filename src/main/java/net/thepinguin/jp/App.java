package net.thepinguin.jp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.reflections.Reflections;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import net.thepinguin.jp.cmd.ICommand;
import net.thepinguin.jp.cmd.Verbose;


/**
 * Main entry point
 */
public class App {
	// common values
	public static final File JP_HOME = new File(System.getProperty("user.home"), ".jp");
	public static final String EOL = System.getProperty("line.separator");
	
	// global command lookup
	private static Map<String, ICommand> _commands;
	
	/**
	 * List of available commands (internal use only); for cli use getCallableCommands() instead
	 * @return list of available commands
	 */
	public static Map<String, ICommand> getAllCommands() {
		return _commands;
	}
	
	/**
	 * Filtered list of commands, only callable returned
	 * @return callable commands
	 */
	public static Map<String, ICommand> getCallableCommands() {
		Map<String, ICommand> ret = new HashMap<String, ICommand>(_commands.size());
		for (Entry<String, ICommand> es : _commands.entrySet()){
			if(es.getValue().isCallable()) {
				ret.put(es.getKey(), es.getValue());
			}
		}
		return ret;
	}
	
	/**
	 * Get list of commands by reflecting on specific package
	 * @return invoked list of commands with string lookup index
	 */
	private static Map<String, ICommand> getReflectedCommands() {
		Map<String, ICommand> ret;
		// get commands via reflection
		Reflections reflections = new Reflections("net.thepinguin.jp.cmd");
		Set<Class<? extends ICommand>> classes = reflections.getSubTypesOf(ICommand.class);
		ret = new HashMap<String, ICommand>(classes.size());
		// iterate classes
		for (Class<? extends ICommand> cmd : classes) {
			// invoke and append to command list
			try {
				ICommand q = cmd.newInstance();
				ret.put(q.getId(), q);
			} catch (InstantiationException e) {
//					e.printStackTrace();
			} catch (IllegalAccessException e) {
//					e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * Application entry point
	 * @param argv cli commands
	 */
	public static void main(String[] argv) {
		try {
			// collect commands via reflection
			_commands = getReflectedCommands();
			List<LongOpt> longopts = new ArrayList<LongOpt>();
			String optstring = "";
			// iterate available commands
			for(Entry<String, ICommand> cmd : _commands.entrySet()) {
				ICommand c = cmd.getValue();
				// only use callable
				if(c.isCallable()){
					LongOpt l = c.getLongOptInstance();
					if(l != null){
						longopts.add(l);
						optstring += c.getOptString();
						//TODO: check for vowel collisions
					}
				}
			}
			
			// handle cli arguments/ command here
			Getopt g = new Getopt("jp", argv, optstring, longopts.toArray(new LongOpt[0]));
			g.setOpterr(false);
			int c;
			while ((c = g.getopt()) != -1) {		
				boolean handled = false;
				if(c != '?'){
					// iterate commands
					for(ICommand cmd : _commands.values() ){	
						if(cmd.getOptVowel() == c){
							handled = cmd.handleOpt(g.getOptarg());
						}
						// terminate program after specific command has handled arguments (help)
						if (handled && cmd.exitAfterHandleOpt()){
							// not exitting, but returning (test will fail otherwise)
							return;
						}
					}
				}
				// check if given argument is handled, else throw exception
				if(!handled){
					throw new Exception("argument not found: -" + (char) g.getOptopt());
				}
			}
			// message on verbosity
			if(App.isVerbose()) {
				System.out.println(" ### VERBOSE output");
			}
			// get cli arguments
			List<String> args = new ArrayList<String>(argv.length);
			for (int i = g.getOptind(); i < argv.length; i++) {
				args.add(argv[i]);
			}
			// iterate implemented commands, pass cli args to them
			boolean handled = false;
			for(ICommand cmd : _commands.values()){
				if(cmd.canHandle(args)){
					cmd.handle(args);
					if(cmd.isHandled()){
						if(App.isVerbose()){
							System.out.println(" ### handling cmd found: " + cmd.getId());
						}
						handled = true;
						break;
					}
				}
			}
			// throw exception when unknown command is given
			if(!handled) {
				if(args.size() <= 1)
					throw new Exception("command/ options missing");
				else
					throw new Exception("invalid command/ option: " + args.get(1));
			}
		} catch(Exception e) {
			if(App.isVerbose()) {
				System.out.println(" ### -> ");
				System.out.println("`" + e.getMessage() + "`");
				e.printStackTrace();
				System.out.println(" ### <- ");
			}
			System.out.println("jp: " + e.getMessage() + " (try -h)");
			System.exit(1);
		}
	}

	/**
	 * Get application verbosity state
	 * @return verbose state
	 */
	public static boolean isVerbose() {
		Map<String, ICommand> cs = getCallableCommands();
		if(cs == null)
			return false;
		ICommand cmd = cs.get("verbose");
		if(cmd == null)
			return false;
		Verbose v = (Verbose) cmd;
		return v.isActive();
	}
	
	/**
	 * Get version of the application
	 * @return version number of application
	 */
	public static String getVersion() {
		try{
			InputStream is = Thread.currentThread().getClass().getResourceAsStream("/jp.properties");
			Properties p = new Properties();
			p.load(is);
			return p.getProperty("version");
		} catch(IOException e) {
			// TODO: this should never fail!
		}
		return "";
	}
	
	/**
	 * Get build of the application (git commit)
	 * @return build number of application
	 */
	public static String getBuild() {
		try{
			InputStream is = Thread.currentThread().getClass().getResourceAsStream("/jp.properties");
			Properties p = new Properties();
			p.load(is);
			return p.getProperty("build");
		} catch(IOException e) {
			// TODO: this should never fail!
		}
		return "";
	}
	
}