package net.thepinguin.jp;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

import net.thepinguin.jp.xml.SaxHandler;

public class AddRepository {

	public static void main(String[] args) {
		try{
//			String pom_xml = args[0];
			
			// read from xml file
			String pomXml = "/Users/dennisb/Programming/github/test/pom.xml";
			SAXParserFactory factory = SAXParserFactory.newInstance();
			InputStream xmlInput  = new FileInputStream(pomXml);
			SAXParser saxParser = factory.newSAXParser();
			SaxHandler handler = new SaxHandler();
			saxParser.parse(xmlInput, handler);
			
			System.out.println(handler.toString());
			
			// list dependencies
//			for(String dependency : handler.getDependencies())
//				System.out.println(dependency);
			
			//TODO: test existance of repository
			//TODO: append repository
			//TODO: write changes to pom_xml
		}catch(Exception e){
			e.printStackTrace ();
		}
	}

}
