package net.thepinguin.jp;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
//		System.out.println("pom: " + pomLocation);
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( pomLocation );
		request.setGoals( goals );
		// silent output
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(Common.M3_HOME);
		System.out.println("M3_HOME " + Common.M3_HOME.getAbsolutePath());
		// silent output
		if(!App.verbose()){
			request.setOutputHandler(null);
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
		if(!App.verbose()){
			request.setOutputHandler(null);
		}
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(Common.M3_HOME);
		System.out.println("M3_HOME2 " + Common.M3_HOME.getAbsolutePath());
		try {
			invoker.execute( request );
			return true;
		} catch (MavenInvocationException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			_errors.add(e.getMessage());
			return false;
		}
	}
	
//	 mvn archetype:generate -DgroupId=net.thepinguin.jptest-1 -DartifactId=jptest-1 -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
}
