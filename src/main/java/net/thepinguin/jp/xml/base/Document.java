package net.thepinguin.jp.xml.base;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import net.thepinguin.jp.xml.pom.Visitable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Document extends DefaultHandler {
	String _xml = "";
	Element _root = new Element("root");
	Stack<Element> _stack = new Stack<Element>();
	
	public Document(String xml) {
		_xml = xml;
	}

	public void startDocument() throws SAXException {
		_stack.push(_root);
	}

	public void endDocument() throws SAXException {
		_stack.pop();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		Element e = new Element(qName, new Attribute(attributes), uri, localName);
		Element top = _stack.peek();
		top.addElement(top, e);
		_stack.push(e);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		_stack.pop();
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {
		String s = String.copyValueOf(ch, start, length).trim();
		Element top = _stack.peek();
		top.setValue(s);
	}

	public void ignorableWhitespace(char ch[], int start, int length)
			throws SAXException {
//		System.out.println(ch);
	}

	public String toString(){
		return _root.toString(0);
	}

	public <T> List<Visitable<T>> findElement(Visitable<T> visitor) {
		return _root.findElement(visitor);
	}

	@SuppressWarnings("restriction")
	public void write() {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			FileWriter f = new FileWriter(_xml);
			XMLStreamWriter writer = factory.createXMLStreamWriter(f);
			writer.writeStartDocument();
			writer.writeCharacters("\n");
			_root.write(writer, -1);
			writer.writeEndDocument();
			writer.flush();
		    writer.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
