package net.thepinguin.jp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
//    		System.out.println(args.length);
//    		for(String s : args)
//    			System.out.println(s);
    		if(args.length <= 1){
    			throw new Exception("invalid command.");
    		}
    		List<String> cs = Arrays.asList(args);
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
        		if(cmd.canHandle(cs)){
        			cmd.handle(cs);
        			if(cmd.isHandled()){
        				handled = true;
        				break;
        			}
        		}
        	}
        	if(!handled){
        		throw new Exception("invalid command: " + cs.get(0));
        	}
    	} catch(Exception e) {
            System.out.println("jp: " + e.getMessage());
            System.out.println("error occured");
    		System.exit(1);
    	}
    }
}