package net.thepinguin.jp.cmd;

import java.io.File;
import java.util.List;

import gnu.getopt.LongOpt;
import net.thepinguin.jp.App;

public class Status implements ICommand {

	private boolean _handled = false;
	
	public int compareTo(ICommand o) {
		if(this.getId() == o.getId())
			return 0;
		else
			return 1;
	}

	public boolean canHandle(List<String> cs) {
		if (cs.size() > 1)
			return cs.get(1).equals(this.getId());
		return false;
	}

	public boolean isHandled() {
		return _handled;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean hasOptions() {
		return true;
	}

	public boolean hasArguments() {
		return false;
	}

	public String getId() {
		return "status";
	}

	public String getDescription() {
		return "Prints status of current project";
	}

	public void handle(List<String> args) throws Exception {
		if(args.size() <= 1){
			throw new Exception("status: missing argument");
		}
		String pwd = args.get(0);		
		File target = new File(pwd, "JPacker");
		if(App.isVerbose()) {
			System.out.println(" ... pwd: " + pwd);
			System.out.println(" ... target: " + target.getName());
		}
		if(!target.exists())
			throw new Exception("not a jp repository, missing: JPacker");
		
		
		
		System.out.println("asdf");
		// TODO: implement status output!
		
		_handled = true;
	}

	public LongOpt getLongOptInstance() {
		return new LongOpt("status", LongOpt.NO_ARGUMENT, null, this.getOptVowel());
	}

	public String getOptString() {
		return "";
	}

	public char getOptVowel() {
		return 0;
	}

	public boolean handleOpt(String optarg) {
		return true;
	}

	public boolean exitAfterHandleOpt() {
		return false;
	}

}
