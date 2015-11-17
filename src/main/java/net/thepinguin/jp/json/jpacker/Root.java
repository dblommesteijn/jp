package net.thepinguin.jp.json.jpacker;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

public class Root {
	

	public Root(String string) {
	}

	public static Root factory(String jsonRaw) {
		try{
			return (new Gson()).fromJson(jsonRaw, Root.class);
		}catch(JsonSyntaxException e){
//			e.printStackTrace();
//			System.out.println("JPacker syntax error");
			return null;
		}
	}
	
	@SerializedName("dependencies")
	public List<Dependency> dependencies;
	
//	public List<Repository>
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(Dependency d : dependencies)
			sb.append(d);
		sb.append(" ]");
		return sb.toString();
	}

	public List<Dependency> resolveDependencies(PrintStream out) {
		List<Dependency> ret = new LinkedList<Dependency>();
		if(out != null) out.println("Resolving...");
		for(Dependency d : dependencies){
			if(out != null) out.print(d.getCannonicalName() + ".");
			if(d.resolve()){
				File loc = d.getFile();
				if(loc != null)
					ret.add(d);
				if(out != null) 
					out.print("OK\n");
			} else
				if(out != null) out.print("ERR\n");
		}
		if(out != null){
//			if(dependencies.isEmpty()) out.println(""); 
			out.println("DONE");
		}
		return ret;
	}
	
	public boolean isValid(){
		List<Boolean> ret = new ArrayList<Boolean>(20);
		if(dependencies != null){
			for(Dependency d : dependencies){
				ret.add(d.isValid());
			}
		}
		else
			ret.add(false);
		return !ret.contains(false);
	}

	public List<String> getErrorMessages() {
		List<String> ret = new ArrayList<String>(5);
		for(Dependency d : dependencies)
			ret.addAll(d.getErrorMessages());
		return ret;
	}
	
}
