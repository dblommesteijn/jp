package net.thepinguin.jp.helper;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import net.thepinguin.jp.App;

public class Mvn {
	
	private static final File M3_HOME = new File(System.getenv("M3_HOME"));
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
		if(!App.isVerbose()){
			request.setOutputHandler(null);
		}
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(M3_HOME);
		invoker.setMavenExecutable(new File(M3_HOME, "bin/mvn"));
		// silent output
		if(!App.isVerbose()){
			invoker.setOutputHandler(null);
		}
		try {
			invoker.execute( request );
			return true;
		} catch (MavenInvocationException e) {
			_errors.add(e.getMessage());
			return false;
		}
	}
	
	public static boolean newProject(String basedir, String groupId, String artifactId) {
		InvocationRequest request = new DefaultInvocationRequest();
		String[] goals = new String[]{"archetype:generate", "-DgroupId="+groupId, 
			"-DartifactId="+artifactId, "-DarchetypeArtifactId=maven-archetype-quickstart",
			"-DinteractiveMode=false"};
		request.setBaseDirectory(new File(basedir));
		request.setGoals(Arrays.asList(goals));
		// silent output
		if(!App.isVerbose()){
			request.setOutputHandler(null);
		}
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(M3_HOME);
		invoker.setMavenExecutable(new File(M3_HOME, "bin/mvn"));
		// silent output
		if(!App.isVerbose()){
			invoker.setOutputHandler(null);
		}
		try {
			invoker.execute( request );
			return true;
		} catch (MavenInvocationException e) {
			System.out.println(e.getMessage());
//			e.printStackTrace();
			_errors.add(e.getMessage());
			return false;
		}
	}
	
}
