package net.thepinguin.jp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import net.thepinguin.jp.json.ParseJP;
import net.thepinguin.jp.json.jpacker.Dependency;
import net.thepinguin.jp.json.jpacker.Root;
import net.thepinguin.jp.xml.Walker;
import net.thepinguin.jp.xml.base.Document;
import net.thepinguin.jp.xml.base.Element;
import net.thepinguin.jp.xml.pom.Visitable;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * Main entry point
 */
public class App
{
    public static void main( String[] args )
    {    	
    	try{
    		String action = args[0];
        	String pomXml = args[1];
        	
        	if(action.equals("add_default_repository")){
        		App.addDefaultRepository(pomXml);
        	} else if(action.equals("collect")){
        		String jpacker = args[2];
//        		String base = new File(jpacker);
//        		String base = FilenameUtils.getPath(jpacker);
        		App.collect(pomXml, jpacker);
        	} else{
        		throw new Exception();
        	}
    	}catch(Exception e){
    		e.printStackTrace();
//    		System.out.println(e.getMessage());
    		System.exit(1);
    	}


    }
    
    static void addDefaultRepository(String pomXml) throws Exception{
		// parse file
		Walker walker = Walker.parseFromFile(pomXml);
		Document doc = walker.getDocument();
		// find project element
		List<Element> pss = doc.findElement("project");
		if(pss.isEmpty()){
			throw new Exception("invalid pom!");
		}
		Element ps = pss.get(0);
		// find repositories
		List<Element> dss = ps.findElement("repositories");
		Element rs = null;
		if(dss.isEmpty()){
			// create new repositories element
			rs = new Element("repositories");
			ps.addElement(ps, rs);
		}
		else{
			// use found repositories element
			rs = dss.get(0);
		}
		// find repository
		boolean foundRepository = false;
		// stuff that needs adding/ finding
		String id = "project.local";
		String name = "project";
		String url = "file:${project.basedir}/repo";
		// iterate dependencies
		for(Element d : rs.getElements()){
			Element q = d.getElementByName("id");
			if(q != null)
				if(q.getValue().equals(id))
					foundRepository = true;
		}
		// add default repository if not found
		if(!foundRepository){
			Element dep_id = new Element("id", id);
			Element dep_name = new Element("name", name);
			Element dep_url = new Element("url", url);
			Element dep = new Element("repository");
			dep.addElementSelf(dep_id).addElementSelf(dep_name).addElementSelf(dep_url);
			rs.addElementSelf(dep);
		}
		// write changes to pom.xml
		doc.write();
    }
    
    static void collect(String pomXml, String jpacker) throws Exception{
    	// parse pom file
		Walker walker = Walker.parseFromFile(pomXml);
		Document doc = walker.getDocument();
		// find project element
		List<Element> pss = doc.findElement("project");
		if(pss.isEmpty()){
			throw new Exception("invalid pom!");
		}
		Element project = pss.get(0);
		List<Element> dss = project.findElement("dependencies");
		Element deps = null;
		if(dss.isEmpty()){
			// create new repositories element
			deps = new Element("dependencies");
			project.addElementSelf(deps);
		}
		else{
			// use found repositories element
			deps = dss.get(0);
		}

		deps.removeAll();
		// parse jpacker file
    	Root root = ParseJP.parseFromFile(jpacker);
    	if(root == null && root.isValid())
    		throw new Exception("invalid JPacker");
    	if(root.dependencies == null)
    		throw new Exception("missing dependencies list");
    	// resolve dependencies
    	System.out.println("jp: collecting...");
    	for(Dependency d : root.dependencies){
    		System.out.print("  " + d.getArtifactId() + ".");
    		if(d.isGithub()){
    			System.out.print("(" + d.github + "#" + d.getCommit() + ")");
    		} else if (d.isFile()){
    			System.out.print("(" + d.file + ")");
    		} else if (d.isBuildIn()){
    			System.out.print("(buildin)");
    		}
    		System.out.print(".");
    		d.resolve();
    		System.out.print(".");
    		if(!d.isValid()){
    			System.out.println(" FAIL");
    			continue;
    		}
    		// deploy dependency to local repository
    		if(d.getFile() != null && d.deployToProjectRepo(pomXml, FilenameUtils.getFullPath(jpacker))){
    			System.out.print(".");
    			// add dependency to pom.xml (if not found)
				Element tmp = new Element("dependency");
				tmp.addElementSelf(new Element("groupId", d.getGroupId()));
				tmp.addElementSelf(new Element("artifactId", d.getArtifactId()));
				tmp.addElementSelf(new Element("version", d.getVersion()));
				tmp.addElementSelf(new Element("scope", d.getScope()));
				deps.addElement(tmp);
    			System.out.println(" OK");
    		} else if(d.isBuildIn()){
    			System.out.print("2");
    			Element tmp = new Element("dependency");
				tmp.addElementSelf(new Element("groupId", d.getGroupId()));
				tmp.addElementSelf(new Element("artifactId", d.getArtifactId()));
				tmp.addElementSelf(new Element("version", d.getVersion()));
				tmp.addElementSelf(new Element("scope", d.getScope()));
				deps.addElement(tmp);
				System.out.println(" OK");
    		} else {
    			//TODO: this is dirty! move errors into dependency!
    			System.out.println(Mvn.getErrorMessages());
    			Mvn.resetErrorMessages();
    		}
    	}
    	// write pom.xml with dependencies
    	if(doc.write())
    		System.out.println("finished");
    	else
    		System.out.println("failed");
    }
}