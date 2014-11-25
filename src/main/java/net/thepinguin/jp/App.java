package net.thepinguin.jp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

import net.thepinguin.jp.cmd.ICommand;

/**
 * Main entry point
 */
public class App
{
    public static void main( String[] args )
    {    	
    	try{
    		// collect active commands implements ICommand
    		Reflections reflections = new Reflections("net.thepinguin.jp.cmd");
        	Set<Class<? extends ICommand>> classes = reflections.getSubTypesOf(ICommand.class);
        	List<ICommand> cmds = new ArrayList<ICommand>(classes.size());
        	// invoke classes
        	for(Class<? extends ICommand> cmd : classes){
        		cmds.add(cmd.newInstance());
        	}
        	// push through commands
        	boolean handled = false;
        	for(ICommand cmd : cmds){
        		if(cmd.canHandle(args)){
        			cmd.handle(args);
        			if(cmd.isHandled()){
        				handled = true;
        				break;
        			}
        		}
        	}
        	if(!handled){
        		throw new Exception("invalid command");
        	}
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    		System.exit(1);
    	}
    }
}