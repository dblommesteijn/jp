package net.thepinguin.jp.cmd;

import java.util.List;

public interface ICommand {

	public boolean canHandle(List<String> cs);
	public void handle(List<String> args) throws Exception;
	public boolean isHandled();
	
}
