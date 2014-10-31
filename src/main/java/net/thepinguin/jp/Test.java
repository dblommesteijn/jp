package net.thepinguin.jp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Test {
	public static void main(String[] args){
		System.out.println("blaat");
		
		
		try {
			String filename = "/Users/dennisb/Programming/github/test/JPacker";
			
			// read from file
			String all = "";
			List<String> ss = Files.readAllLines(Paths.get(filename));
			for(String s : ss){
				all += s;
			}
			
			// parse JSON
			Object obj = JSONValue.parse(all);
			JSONObject root = (JSONObject) obj;
			JSONArray deps = (JSONArray) root.get("dependencies");
			
			for(Object d : deps){
				JSONObject jo = (JSONObject) d;
				String name = (String) jo.get("name");
				String version = (String) jo.get("version");
				String file = (String) jo.get("file");
				System.out.println("name: `" + name + "`, version: `" + version + "`, file: `" + file + "`");
			}
			
			
			
			
			
				
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
