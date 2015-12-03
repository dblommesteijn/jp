package net.thepinguin.jp.cmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import gnu.getopt.LongOpt;
import net.thepinguin.jp.App;
import net.thepinguin.jp.helper.Mvn;
import net.thepinguin.jp.model.JP;

public class New implements ICommand {
	
	private boolean _handled = false;

	public int compareTo(ICommand o) {
		if(this.getId() == o.getId())
			return 0;
		else
			return 1;
	}

	public boolean canHandle(List<String> cs) {
		if (cs.size() > 1)
			return cs.get(1).equals(this.getId());
		return false;
	}

	public boolean isHandled() {
		return _handled;
	}

	public boolean isEnabled() {
		return true;
	}

	public String getId() {
		return "new";
	}

	public String getDescription() {
		return "Create a new jp enabled project";
	}
	
	public void handle(List<String> args) throws Exception {
		if(args.size() <= 2){
			throw new Exception("new: missing argument");
		}
		String pwd = args.get(0);
		JP jp = new JP(new File(pwd));
//		JP jp = JP.createNewProject(new File(pwd), "");
		jp.setGroupId(args.get(2).toLowerCase());
		jp.setArtifactId("");
		// has no artifactId, only groupId (net.thepinguin.artifactId -> artifactId)
		if(args.size() <= 3) {
			String[] ns = jp.getGroupDomain();
			if(ns.length <= 1){
				throw new Exception("unexpected groupId: " + jp.getGroupId());
			}
			// TODO: add regex for valid namespacing
//			String regx = "[a-zA-Z]+\\.?";
//			Pattern pattern = Pattern.compile(regx);
//			System.out.println(pattern);
			jp.setArtifactId(ns[ns.length -1]);
		} else {
			// TODO: add regex for valid namespacing
			jp.setArtifactId(args.get(3).toLowerCase());
		}
		
//		jp.setCwd(new File(pwd, jp.getArtifactId()));
		if(App.isVerbose()) {
			System.out.println(" ... groupId:    " + jp.getGroupId());
			System.out.println(" ... artifactId: " + jp.getArtifactId());
		}
		// create maven project
		if(jp.createNewProject()){
			_handled = true;
		} else {
			//TODO: throw maven errors here?
			throw new Exception("maven error");
		}
		// reload jp with new basedir (pwd/ cwd)
		jp = jp.factory(new File(pwd, jp.getArtifactId()));
		// create target JPacker file
		jp.createJPacker();
		
		Repository r = (Repository) App.getAllCommands().get("repo");
		// call jp repo with [0] = /path/to/jp/enabled/repo
		r.handle(Arrays.asList(new String[]{new File(pwd, jp.getArtifactId()).getAbsolutePath()}));
		
		System.out.println("... project created: " + jp.getArtifactId());
		System.out.println("... collecting deps for: " + jp.getArtifactId());
		
		// load new command
		Collect c = (Collect) App.getAllCommands().get("collect");
		// call jp collect with [0] = /path/to/jp/enabled/repo
		c.handle(Arrays.asList(new String[]{new File(pwd, jp.getArtifactId()).getAbsolutePath()}));
	}

	public String getOptString() {
		return "";
	}

	public LongOpt getLongOptInstance() {
		return new LongOpt("new", LongOpt.NO_ARGUMENT, null, this.getOptVowel());
	}

	public char getOptVowel() {
		return 0;
	}
	
	public boolean handleOpt(String optarg) {
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
	
	public String toString() {
		return "{" + this.getId() + ":"+ this.getDescription() + "}";
	}
	
	public boolean isCallable() {
		return this.hasOptions() || this.hasArguments();
	}
	
}
