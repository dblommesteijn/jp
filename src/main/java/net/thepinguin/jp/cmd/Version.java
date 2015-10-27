package net.thepinguin.jp.cmd;

import java.util.List;

import gnu.getopt.LongOpt;
import net.thepinguin.jp.App;

public class Version implements ICommand {

	private boolean _handled = false;
	
	@Override
	public int compareTo(ICommand o) {
		if(this.getId() == o.getId())
			return 0;
		else
			return 1;
	}

	@Override
	public boolean canHandle(List<String> cs) {
		if (cs.size() > 1)
			return cs.get(1).equals(this.getId());
		return false;
	}

	@Override
	public boolean isHandled() {
		return _handled;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean hasOptions() {
		return true;
	}

	@Override
	public boolean hasArguments() {
		return true;
	}

	@Override
	public String getId() {
		return "version";
	}

	@Override
	public String getDescription() {
		return "Print application version";
	}

	@Override
	public void handle(List<String> args) throws Exception {
		this.handleOpt(null);
		_handled = true;
	}

	@Override
	public LongOpt getLongOptInstance() {
		return new LongOpt("version", LongOpt.NO_ARGUMENT, null, this.getOptVowel());
	}

	@Override
	public String getOptString() {
		return "V";
	}

	@Override
	public char getOptVowel() {
		return 'V';
	}

	@Override
	public boolean handleOpt(String optarg) {
		System.out.println("jp: version: " + App.getVersion());
		return true;
	}

	@Override
	public boolean exitAfterHandleOpt() {
		return true;
	}

}
