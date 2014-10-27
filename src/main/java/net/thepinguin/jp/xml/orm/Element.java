package net.thepinguin.jp.xml.orm;

import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;

public class Element {
	private String _name = "";
	private Attribute _attribute;
	private String _uri = "";
	private String _localName = "";
	
	private Element _parent = null;
	private List<Element> _elements = new LinkedList<Element>();
	private String _value = "";

	public Element(String name, Attribute attribute, String uri, String localName) {
		_name = name;
		_attribute = attribute;
		_uri = uri;
		_localName = localName;
	}

	public Element(String name) {
		_name = name;
	}

	public void addElement(Element parent, Element element) {
		_parent = parent;
		_elements.add(element);
	}

	public Element pop() {
		return _parent;
	}

	public String getName() {
		return _name;
	}

	public String getParentName() {
		return _parent.getName();
	}
	
	public String toString(int offset){
		StringBuilder sb = new StringBuilder();
		sb.append("elemnt(").append(offset).append("), ");
		sb.append("name:").append(_name).append(", ");
		sb.append("value: ").append(_value).append(", ");
		sb.append("attrib: [").append(_attribute).append("],");
		sb.append("\n");
		for(Element e : _elements)
			sb.append(e.toString(offset+1));
//		sb.append("").append(offset).append(")\n");
		return sb.toString();
	}

	public void setValue(String value) {
		_value = value;
	}

}
