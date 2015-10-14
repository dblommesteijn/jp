package net.thepinguin.jp.cmd;

import java.util.List;

import gnu.getopt.LongOpt;

public interface ICommand extends Comparable<ICommand> {
	
	// generic running after sanity checks
	public boolean canHandle(List<String> cs);
	public boolean isHandled();
	public boolean isEnabled();
	public boolean hasOptions();
	public boolean hasArguments();
	
	// command specific info
	public String getId();
	public String getDescription();
	public void handle(List<String> args) throws Exception;
	// args (getopts phase)
	public LongOpt getLongOptInstance();
	public String getOptString();
	public char getOptVowel();
	public boolean handleOpt(String optarg);
	public boolean exitAfterHandleOpt();
	// print strings
//	public String getOptString();
//	public String getArgString();
}
