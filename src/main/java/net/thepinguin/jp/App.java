package net.thepinguin.jp;

import java.util.ArrayList;
import java.util.List;

import net.thepinguin.jp.xml.Walker;
import net.thepinguin.jp.xml.base.Document;
import net.thepinguin.jp.xml.base.Element;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * Hello world!
 *
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
        	}
        	else{
        		throw new Exception();
        	}
    	}catch(Exception e){
    		System.out.println(e.getMessage());
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
}