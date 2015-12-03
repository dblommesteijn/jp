package net.thepinguin.jp.cmd;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.thepinguin.jp.helper.Mvn;
import net.thepinguin.jp.json.ParseJP;
import net.thepinguin.jp.json.jpacker.Dependency;
import net.thepinguin.jp.json.jpacker.Root;
import net.thepinguin.jp.xml.Walker;
import net.thepinguin.jp.xml.base.Document;
import net.thepinguin.jp.xml.base.Element;

import org.apache.commons.io.FilenameUtils;

import gnu.getopt.LongOpt;

public class Collect implements ICommand {

	private boolean _handled = false;

	public boolean canHandle(List<String> args) {
		if (args.size() > 1)
			return args.get(1).equals(this.getId());
		return false;
	}
	
	public int compareTo(ICommand o) {
		if(this.getId() == o.getId())
			return 0;
		else
			return 1;
	}
	
	public boolean isEnabled() {
		return true;
	}
	
	public boolean isHandled() {
		return _handled;
	}

	public String getId() {
		return "collect";
	}
	
	public String getDescription(){
		return "Collect dependencies";
	}

	public void handle(List<String> args) throws Exception {
		String pwd = args.get(0);
		File pomXml = new File(pwd, "pom.xml");
		File jpacker = new File(pwd, "JPacker");
				
		// parse pom file
		if(!pomXml.exists()){
			throw new Exception("pom.xml file not found");
		}
		Walker walker = Walker.parseFromFile(pomXml);
		Document doc = walker.getDocument();
		// find project element
		List<Element> pss = doc.findElement("project");
		if (pss.isEmpty()) {
			throw new Exception("invalid pom!");
		}
		// append dependencies
		Element project = pss.get(0);
		List<Element> dss = project.findElement("dependencies");
		Element deps = null;
		if (dss.isEmpty()) {
			// create new repositories element
			deps = new Element("dependencies");
			project.addElementSelf(deps);
		} else {
			// use found repositories element
			deps = dss.get(0);
		}
		deps.removeAll();
		// parse jpacker file
		Root root = ParseJP.parseFromFile(jpacker);
		if (root == null)
			throw new Exception("JPacker file not found");
		if (!root.isValid())
			throw new Exception("JPacker file corrupt");
		if (root.dependencies == null)
			throw new Exception("missing dependencies list");
		// resolve dependencies
		System.out.println("jp: collecting...");
		for (Dependency d : root.dependencies) {
			if (d == null)
				continue;
			System.out.print("  " + d.getArtifactId() + " ");
			if (d.isGit()) {
				System.out.print("(" + d.git + "#" + d.getCommit() + ")");
			} else if (d.isFile()) {
				System.out.print("(" + d.file + ")");
			} else if (d.isBuildIn()) {
				System.out.print("(buildin)");
			}
			System.out.print(".");
			d.resolve();
			System.out.print(".");
			if (!d.isValid()) {
				System.out.println(" FAIL");
				System.out.println(d.getErrorMessages());
				continue;
			}
			// deploy dependency to local repository
			if (d.getFile() != null
					&& d.deployToProjectRepo(pomXml,
							FilenameUtils.getFullPath(jpacker.getAbsolutePath()))) {
				System.out.print(".");
				// add dependency to pom.xml (if not found)
				Element tmp = new Element("dependency");
				tmp.addElementSelf(new Element("groupId", d.getGroupId()));
				tmp.addElementSelf(new Element("artifactId", d.getArtifactId()));
				tmp.addElementSelf(new Element("version", d.getVersion()));
				tmp.addElementSelf(new Element("scope", d.getScope()));
				deps.addElement(tmp);
				System.out.println(" OK");
			} else if (d.isBuildIn()) {
				Element tmp = new Element("dependency");
				tmp.addElementSelf(new Element("groupId", d.getGroupId()));
				tmp.addElementSelf(new Element("artifactId", d.getArtifactId()));
				tmp.addElementSelf(new Element("version", d.getVersion()));
				tmp.addElementSelf(new Element("scope", d.getScope()));
				deps.addElement(tmp);
				System.out.println(" OK");
			} else {
				System.out.println(d.getErrorMessages());
				System.out.println("error");
			}
		}
		
		List<Element> rss = project.findElement("repositories");
		Element rs = null;
		if(rss.isEmpty()){
			// create new repositories element
			rs = new Element("repositories");
			project.addElement(project, rs);
		}
		else{
			// use found repositories element
			rs = rss.get(0);
		}
		
		
		// write pom.xml with dependencies & repositories
		if (doc.write()) {
			// build pom.xml
			boolean b = Mvn.invokeMaven(pomXml, Arrays.asList("install"));
			if (b) {
				System.out.println("jp: finished");
			} else {
				System.out.println("jp: " + Mvn.getErrorMessages());
				System.out.println("build failed");
				System.exit(1);
			}
		} else {
			throw new Exception("write failed: " + pomXml);
		}
		_handled = true;
	}

	public String getOptString() {
		return "";
	}

	public LongOpt getLongOptInstance() {
		return new LongOpt("collect", LongOpt.NO_ARGUMENT, null, this.getOptVowel());
	}

	public char getOptVowel() {
		return 0;
	}
	
	public boolean handleOpt(String optarg) {
		System.out.println(optarg);
		return true;
	}
	
	public boolean exitAfterHandleOpt() {
		return false;
	}
	
	public boolean hasOptions() {
		return true;
	}
	
	public boolean hasArguments() {
		return false;
	}
	
	public boolean isCallable() {
		return this.hasOptions() || this.hasArguments();
	}

}
