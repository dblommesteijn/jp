package net.thepinguin.jp.json;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import net.thepinguin.jp.json.jpacker.Root;

public class ParseJP {

	public static Root parseFromFile(String filename) throws IOException {
		// read from file
		StringBuilder sb = new StringBuilder();
		FileInputStream inputStream = new FileInputStream(filename);
	    try {
	        sb.append(IOUtils.toString(inputStream));
	    } finally {
	        inputStream.close();
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
