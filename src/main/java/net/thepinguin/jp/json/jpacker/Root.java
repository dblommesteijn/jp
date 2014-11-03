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
	
}
