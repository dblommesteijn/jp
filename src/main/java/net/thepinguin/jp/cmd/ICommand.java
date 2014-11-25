package net.thepinguin.jp.cmd;

public interface ICommand {

	public boolean canHandle(String[] args);
	public void handle(String[] args) throws Exception;
	public boolean isHandled();
	
}
