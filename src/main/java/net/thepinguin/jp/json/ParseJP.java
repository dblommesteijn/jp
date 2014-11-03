package net.thepinguin.jp.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;

import net.thepinguin.jp.json.jpacker.Root;

public class ParseJP {

	public static Root parseFromFile(String filename) throws IOException {
		// read from file
		StringBuilder sb = new StringBuilder();
		List<String> ss = Files.readAllLines(Paths.get(filename));
		for(String s : ss){
			sb.append(s);
		}
		return Root.factory(sb.toString());
	}
	
	public String toJson() {
		return (new Gson()).toJson(this);
	}

}
