package net.thepinguin.jp.xml.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.Attributes;

public class Attribute {
	
	private Map<String, String> _values = new HashMap<String, String>();

	public Attribute(Attributes attributes) {
		for(int i = 0; i < attributes.getLength(); i++){
			_values.put(attributes.getQName(i), attributes.getValue(i));
		}
	}
	
	public boolean hasValues(){
		return !_values.isEmpty();
	}
	
	public Map<String, String> getValues(){
		return _values;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> s : _values.entrySet()){
			sb.append(s.getKey()).append(":").append(s.getValue()).append(", ");
		}
		return sb.toString();
	}
}
