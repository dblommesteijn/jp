package net.thepinguin.jp.cmd;

import java.util.List;

import gnu.getopt.LongOpt;

public class New implements ICommand {
	
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

	public String getId() {
		return "new";
	}

	public String getDescription() {
		return "Create a new jp enabled project";
	}
	
	public void handle(List<String> args) throws Exception {
		System.out.println("handling new...");
		_handled = true;
	}

	public String getOptString() {
		return "";
	}

	public LongOpt getLongOptInstance() {
		return new LongOpt("new", LongOpt.NO_ARGUMENT, null, this.getOptVowel());
	}

	public char getOptVowel() {
		return 0;
	}
	
	public boolean handleOpt(String optarg) {
		System.out.println(optarg);
		return true;
	}
	
	public boolean exitAfterHandleOpt() {
		return false;
	}
	
	public boolean hasOptions() {
		return true;
	}
	
	public boolean hasArguments() {
		return false;
	}
	
	public String toString() {
		return "{" + this.getId() + ":"+ this.getDescription() + "}";
	}
	
}
