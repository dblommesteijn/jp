package net.thepinguin.jp.cmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import gnu.getopt.LongOpt;
import net.thepinguin.jp.App;
import net.thepinguin.jp.Mvn;

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
		String groupId = args.get(2).toLowerCase();
		String artifactId = "";
		// has no artifactId, only groupId (net.thepinguin.artifactId -> artifactId)
		if(args.size() <= 3) {
			String[] ns = groupId.split("[.]");
			if(ns.length <= 1){
				throw new Exception("unexpected groupId: " + groupId);
			}
			// TODO: add regex for valid namespacing
//			String regx = "[a-zA-Z]+\\.?";
//			Pattern pattern = Pattern.compile(regx);
//			System.out.println(pattern);
			artifactId = ns[ns.length -1];
		} else {
			// TODO: add regex for valid namespacing
			artifactId = args.get(3).toLowerCase();
		}
		if(App.isVerbose()) {
			System.out.println("groupId:    " + groupId);
			System.out.println("artifactId: " + artifactId);
		}
		// create maven project
		if(Mvn.newProject(pwd, groupId, artifactId)){
			_handled = true;
		} else {
			throw new Exception("maven error");
		}
		// create JPacker file
		try{
			// build basic dependency (default)
			StringBuilder sb = new StringBuilder();
			sb.append("{\"dependencies\": [");
			sb.append(App.EOL);
			sb.append("  { \"name\": \"junit#junit#4.12\", \"scope\": \"test\" }");
			sb.append(App.EOL);
			sb.append("]}");
			// write to JPacker file
			PrintWriter wrt = new PrintWriter(new File(pwd, artifactId+"/JPacker"));
			wrt.println(sb);
			wrt.close();
		} catch(FileNotFoundException e){
			throw new Exception("unable to create JPacker file");
		}
		System.out.println("... project created: " + artifactId);
		System.out.println("... collecting deps for: " + artifactId);
		// load new command
		Collect c = (Collect) App.getCommands().get("collect");
		// call jp collect with [0] = /path/to/jp/enabled/repo
		c.handle(Arrays.asList(new String[]{new File(pwd, artifactId).getAbsolutePath()}));
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
	
}
