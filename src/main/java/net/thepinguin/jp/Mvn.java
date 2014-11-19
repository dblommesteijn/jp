package net.thepinguin.jp;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class Mvn {
	
	private static List<String> _errors = new LinkedList<String>();
	
	public static List<String> getErrorMessages(){
		return _errors;
	}
	
	public static void resetErrorMessages(){
		_errors.clear();
	}
	
	public static boolean invokeMaven(File pomLocation, List<String> goals ){
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( pomLocation );
		request.setGoals( goals );
		// silent output
		request.setOutputHandler(null);
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(Common.M3_HOME);
		// silent output
		invoker.setOutputHandler(null);
		try {
			invoker.execute( request );
			return true;
		} catch (MavenInvocationException e) {
			_errors.add(e.getMessage());
			return false;
		}
	}
}
