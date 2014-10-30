package net.thepinguin.jp.xml.pom;

import net.thepinguin.jp.xml.base.Element;

public class Repository implements Visitable<Repository> {

	public Visitable<Repository> isTypeOf(Visitable<?> visitor, Element e) {
		// TODO Auto-generated method stub
		return null;
	}

	private Element _element;
	
	public Repository(){
		
	}
	
	public Repository(Element element){
		_element = element;
		//TODO: parse element here!
	}
}
