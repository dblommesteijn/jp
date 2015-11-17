package net.thepinguin.jp.cmd;

import java.util.List;

import gnu.getopt.LongOpt;

public class Repository implements ICommand {

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
		// TODO Auto-generated method stub
		return true;
	}

	public boolean hasOptions() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasArguments() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return "repo";
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public void handle(List<String> args) throws Exception {
		// TODO Auto-generated method stub
//		_handled = true;
		
	}

	public LongOpt getLongOptInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOptString() {
		// TODO Auto-generated method stub
		return null;
	}

	public char getOptVowel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean handleOpt(String optarg) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean exitAfterHandleOpt() {
		return false;
	}

}
