package net.thepinguin.jp.xml.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import net.thepinguin.jp.xml.NotFoundException;
import net.thepinguin.jp.xml.pom.Dependency;
import net.thepinguin.jp.xml.pom.Visitable;

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
	
	public Element(String name, String value){
		_name = name;
		_value = value;
	}

	/**
	 * Add a new Element to the current Element
	 * @param parent specify Element parent
	 * @param element Element to append to current Element
	 * @return reference to appended Element
	 */
	public Element addElement(Element parent, Element element) {
		_parent = parent;
		_elements.add(element);
		return element;
	}
	
	/**
	 * Add a new Element to the current Element
	 * @param element Element to append to current Element
	 * @return reference to appended Element
	 */
	public Element addElement(Element element) {
		_parent = this;
		_elements.add(element);
		return element;
	}
	
	/**
	 * Add a new Element to the current Element
	 * @param element Element to append to current Element
	 * @return reference to itself
	 */
	public Element addElementSelf(Element element){
		_parent = this;
		_elements.add(element);
		return this;
	}
	
	public void removeElement(Element element) {
		_elements.remove(element);
	}
	
	public void removeElement(String name) {
		for(Element e : _elements){
			if(e.getName().equals(name)){
				_elements.remove(e);
				break;
			}
		}
	}

	public String getName() {
		return _name;
	}

	public String getParentName() {
		return _parent.getName();
	}
	
	public String getValue() {
		return _value;
	}
	
	public boolean hasAttributes(){
		return (_attribute != null && _attribute.hasValues());
	}
	
	public Attribute getAttribute(){
		return _attribute;
	}
	
	public boolean hasElements(){
		return !_elements.isEmpty();
	}
	
	public Element getElementByName(String name){
		for(Element e : _elements){
			if(e.getName().equals(name))
				return e;
		}
		return null;
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

	public <T> List<Visitable<T>> findElement(Visitable<T> visitor) {	
		List<Visitable<T>> ret = new LinkedList<Visitable<T>>();
		
//		visitor.isTypeOf((Visitable<?>)visitor, this);
		
		Visitable<T> a = visitor.isTypeOf(visitor, this);
		// append if type is found
		if(a != null){
			ret.add(a);
		}
		// iterate nested elements
		for(Element e : _elements){
			ret.addAll(e.findElement(visitor));
		}
		return ret;
	}
	
	public List<Element> findElement(String name) {
		List<Element> ret = new LinkedList<Element>();
		if(_name.equals(name))
			ret.add(this);
		for(Element e : _elements)
			ret.addAll(e.findElement(name));
		return ret;
	}

	public List<Element> getElements() {
		return _elements;
	}

	@SuppressWarnings("restriction")
	public void write(XMLStreamWriter writer, int depth) throws XMLStreamException {
		//TODO: remove root element check, refactor indentation
		// start element
		if(!_name.equals("root")){
			writer.writeCharacters("\n");
			for(int i=0; i<depth; i++)
				writer.writeCharacters("  ");
			writer.writeStartElement(_name);
			if (this.hasAttributes())
				_attribute.write(writer);
			else{
				writer.writeCharacters(_value);
			}
		}
		// nested elements
		if(!_elements.isEmpty()){
			for(Element e: _elements){
				e.write(writer, depth + 1);
			}
			writer.writeCharacters("\n");
			for(int i=0; i<depth; i++)
				writer.writeCharacters("  ");
		}
		// end element
		if(!_name.equals("root")){
			writer.writeEndElement();
		}
	}

}
