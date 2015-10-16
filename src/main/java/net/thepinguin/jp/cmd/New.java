package net.thepinguin.jp.cmd;

import java.util.List;
import java.util.regex.Pattern;

import gnu.getopt.LongOpt;
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
		String pwd = args.get(0);
		System.out.println("pwd: " + pwd);
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
		System.out.println("groupId:    " + groupId);
		System.out.println("artifactId: " + artifactId);
		
		
		if(Mvn.newProject(pwd, groupId, artifactId)){
			_handled = true;
		} else {
			throw new Exception("maven error");
		}
		
//		 mvn archetype:generate -DgroupId=$GROUP_ID \
//		          -DartifactId=$PROJECT_NAME -DarchetypeArtifactId=maven-archetype-quickstart \
//		          -DinteractiveMode=false &> /dev/null
		
		
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
	
	public String toString() {
		return "{" + this.getId() + ":"+ this.getDescription() + "}";
	}
	
}
