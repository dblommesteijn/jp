package net.thepinguin.jp.cmd;

import java.io.File;
import java.util.List;

import gnu.getopt.LongOpt;
import net.thepinguin.jp.xml.Walker;
import net.thepinguin.jp.xml.base.Document;
import net.thepinguin.jp.xml.base.Element;

public class AddDefaultRepository implements ICommand {
	
	private boolean _handled = false;

	public boolean canHandle(List<String> args) {
		if(args.size() > 0)
			return args.get(0).equals("add_default_repository");
		return false;
	}
	
	public int compareTo(ICommand o) {
		if(this.getId() == o.getId())
			return 0;
		else
			return 1;
	}
	
	public boolean isEnabled() {
		return false;
	}
	
	public void handle(List<String> args) throws Exception {
//    	String pomXml = args.get(1);
		File pomXml = new File(args.get(0));
    	
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
		_handled = true;
	}

	public boolean isHandled() {
		return _handled;
	}

	public String getId() {
		return "";
	}

	public LongOpt getLongOptInstance() {
		return null;
	}
	
	public char getOptVowel() {
		return 0;
	}
	
	public String getDescription() {
		return "";
	}

	public boolean handleOpt(String optarg) {
		return false;
	}
	
	public boolean exitAfterHandleOpt() {
		return false;
	}

	public String getOptString() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasOptions() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasArguments() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCallable() {
		return this.hasOptions() || this.hasArguments();
	}


}
