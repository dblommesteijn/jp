package net.thepinguin.jp.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;

import net.thepinguin.jp.xml.base.Document;
import net.thepinguin.jp.xml.pom.Visitable;

public class Walker {
	
//	public static Walker parseFromFile(File pom) {
//		
//	}

	public static Walker parseFromFile(File pomXml) {
		try{
			//TODO: push sax parser into Document
			// read xml from file
			SAXParserFactory factory = SAXParserFactory.newInstance();
			InputStream xmlInput  = new FileInputStream(pomXml);
			SAXParser saxParser = factory.newSAXParser();
			// parse to document handle
			Document document = new Document(pomXml);
			saxParser.parse(xmlInput, document);
			// return instance of walker containing the document
			return new Walker(document);
		} catch(Exception e){
//			throw new Exception("unable to parse");
//			e.printStackTrace();
		}
		return null;
	}
	
	public static Walker parseFromString(String validXml) {
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			InputStream xmlInput = IOUtils.toInputStream(validXml, "UTF-8");
			SAXParser saxParser = factory.newSAXParser();
			// parse to document handle
			Document document = new Document(validXml);
			saxParser.parse(xmlInput, document);
			// return instance of walker containing the document
			return new Walker(document);
		} catch(Exception e){
//			e.printStackTrace();
		}
		return null;
	}
	
	private Document _document;
	
	public Walker(Document document) {
		_document = document;
	}
	
	public <T> List<Visitable<T>> visit(Visitable<T> visitor){
		return _document.findElement(visitor);
	}
	
	public Document getDocument(){
		return _document;
	}
}
