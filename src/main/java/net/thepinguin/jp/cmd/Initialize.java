package net.thepinguin.jp.cmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import gnu.getopt.LongOpt;
import net.thepinguin.jp.App;
import net.thepinguin.jp.model.JP;

public class Initialize implements ICommand {

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
		return "init";
	}

	public String getDescription() {
		return "Initializes existing maven project with JPacker";
	}

	public void handle(List<String> args) throws Exception {
		// expect two arguments
		if(args.size() <= 1){
			throw new Exception("init: missing argument");
		}
		// init jp model
		JP jp = new JP(new File(args.get(0)));
		// validate or throw exception
		jp.isValidOrThrowException();
		// validate jpacker is not yet present
		File jpacker = jp.getJPacker();
		if(jpacker.exists())
			throw new Exception("JPacker already present, not creating new one");
		// create JPacker file (or throw exception)
		jp.createJPacker();
		_handled = true;
	}

	public LongOpt getLongOptInstance() {
		return new LongOpt("init", LongOpt.NO_ARGUMENT, null, this.getOptVowel());
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
	
	public boolean isCallable() {
		return this.hasOptions() || this.hasArguments();
	}

}
