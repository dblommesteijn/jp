package net.thepinguin.jp.cmd;

import java.util.List;

import gnu.getopt.LongOpt;

public class Verbose implements ICommand {
	
	private boolean _active = false;
	
	public int compareTo(ICommand o) {
		if(this.getId() == o.getId())
			return 0;
		else
			return 1;
	}

	public boolean canHandle(List<String> cs) {
		// verbose cannot handle anything by itself
		return false;
	}

	public boolean isHandled() {
		return false;
	}

	public boolean isEnabled() {
		return true;
	}

	public String getId() {
		return "verbose";
	}

	public String getDescription() {
		return "Enable verbose output";
	}

	public void handle(List<String> args) throws Exception {
		// NOTE: verbose is a state, it cannot handle anything on its own
	}

	public LongOpt getLongOptInstance() {
		return new LongOpt("verbose", LongOpt.NO_ARGUMENT, null, this.getOptVowel());
	}

	public String getOptString() {
		return "v";
	}

	public char getOptVowel() {
		return 'v';
	}

	public boolean handleOpt(String optarg) {
		_active = true;
		return _active;
	}

	public boolean exitAfterHandleOpt() {
		return false;
	}

	public boolean hasOptions() {
		return false;
	}
	
	public boolean hasArguments() {
		return true;
	}
	
	public boolean isActive() {
		return _active;
	}
	
}
