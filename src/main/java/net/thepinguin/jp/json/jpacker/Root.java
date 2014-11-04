package net.thepinguin.jp.json.jpacker;

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
			System.out.println("JPacker syntax error");
			return null;
		}
	}
	
	@SerializedName("dependencies")
	public List<Dependency> dependencies;
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(Dependency d : dependencies)
			sb.append(d);
		sb.append(" ]");
		return sb.toString();
	}

	public void resolveDependencies() {
		System.out.println("Resolving...");
		for(Dependency d : dependencies){
			System.out.print(d.getCannonicalName() + ".");
			if(d.resolve())
				System.out.print("OK\n");
			else
				System.out.print("ERR\n");
		}
	}
	
}
