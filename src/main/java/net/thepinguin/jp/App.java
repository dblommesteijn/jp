package net.thepinguin.jp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private static Map<String, ICommand> _cmds;

	public static Map<String, ICommand> getCommands() {
		return _cmds;
	}

	public static void main(String[] argv) {
		try {
			List<LongOpt> longopts = new ArrayList<LongOpt>();
			String optstring = "";
			// get commands via reflection
			Reflections reflections = new Reflections("net.thepinguin.jp.cmd");
			Set<Class<? extends ICommand>> classes = reflections.getSubTypesOf(ICommand.class);
			_cmds = new HashMap<String, ICommand>(classes.size());
			// iterate classes
			for (Class<? extends ICommand> cmd : classes) {
				// invoke and append to command list
				ICommand q = cmd.newInstance();
				// filter disabled commands
				if (q.isEnabled()) {
					_cmds.put(q.getId(), q);
					LongOpt l = q.getLongOptInstance();
					if(l != null) {
						longopts.add(l);
						optstring += q.getOptString();
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
					for(ICommand cmd : _cmds.values() ){	
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
			if(App.verbose()) {
				System.out.println(" ### VERBOSE output");
			}
			// get cli arguments
			List<String> args = new ArrayList<String>(argv.length);
			for (int i = g.getOptind(); i < argv.length; i++) {
				args.add(argv[i]);
			}
			// iterate implemented commands, pass cli args to them
			boolean handled = false;
			for(ICommand cmd : _cmds.values()){
				if(cmd.canHandle(args)){
					cmd.handle(args);
					if(cmd.isHandled()){
						if(App.verbose()){
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
					throw new Exception("option missing");
				else
					throw new Exception("invalid option: " + args.get(1));
			}
		} catch(Exception e) {
			if(App.verbose()) {
				System.out.println(" ### -> ");
				System.out.println(e.getMessage());
				e.printStackTrace();
				System.out.println(" ### <- ");
			}
			System.out.println("jp: " + e.getMessage() + " (try -h)");
			System.exit(1);
		}
	}

	public static boolean verbose() {
		ICommand cmd = _cmds.get("verbose");
		if(cmd == null)
			return false;
		Verbose v = (Verbose) cmd;
		return v.isActive();
	}
}