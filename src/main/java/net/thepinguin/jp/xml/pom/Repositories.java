package net.thepinguin.jp.xml.pom;

import net.thepinguin.jp.xml.base.Element;

public class Repositories implements Visitable<Repositories> {

	public Visitable<Repositories> isTypeOf(Visitable<?> visitor, Element element) {
		if (element.getName().equals("repositories"))
			return new Repositories(element);
		return null;
	}
	
	private Element _element;
	
	public Repositories(){
		
	}
	
	public Repositories(Element element){
		_element = element;
		//TODO: parse element here!
	}

}
