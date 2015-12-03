package net.thepinguin.jp.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import net.thepinguin.jp.json.jpacker.Root;

public class ParseJP {
	
	public static Root parseFromFile(File file) {
		// read from file
		StringBuilder sb = new StringBuilder();
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
	        sb.append(IOUtils.toString(inputStream));
	        inputStream.close();
		} catch(IOException e){
			return null;
	    }
		return Root.factory(sb.toString());
	}
	
	public String toJson() {
		return (new Gson()).toJson(this);
	}

	public static Root parseFromString(String rawJson) {
		return Root.factory(rawJson);
	}

}
