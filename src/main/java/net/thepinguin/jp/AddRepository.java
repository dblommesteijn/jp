package net.thepinguin.jp;

import java.util.List;

import net.thepinguin.jp.xml.Walker;
import net.thepinguin.jp.xml.pom.Dependency;
import net.thepinguin.jp.xml.pom.Repositories;
import net.thepinguin.jp.xml.pom.Visitable;

public class AddRepository {

	public static void main(String[] args) {
		try{
//			String pom_xml = args[0];
			
			// read from xml file and make tree
			String pomXml = "/Users/dennisb/Programming/github/test/pom.xml";
			Walker walker = Walker.parseFromFile(pomXml);
			
			// visit nodes in walker and lookup
			List<Visitable<Repositories>> repositories = walker.visit(new Repositories());
			if(repositories == null){
				System.out.println("not found");
				System.exit(-1);
			}
			if(repositories.isEmpty()){
				
			}
			
			
			for(Visitable<Repositories> repos : repositories){
				System.out.println(repos);
			}
			
//			for(Visitable<Dependency> dep : dependencies){
//				Dependency d = (Dependency)dep;
//				
//				System.out.println("groupId: " + d.getGroupId());
//				System.out.println("artifactId: " + d.getArtifactId());
//				System.out.println("version: " + d.getVersion());
//				System.out.println("scope: " + d.getScope());
//			}
			
			
			//TODO: test existance of repository
			//TODO: append repository
			//TODO: write changes to pom_xml
		}catch(Exception e){
			e.printStackTrace ();
		}
	}

}
