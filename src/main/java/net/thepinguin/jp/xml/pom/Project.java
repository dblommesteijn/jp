package net.thepinguin.jp.xml.pom;

import net.thepinguin.jp.xml.base.Element;

public class Project implements Visitable<Project> {

	public Visitable<Project> isTypeOf(Visitable<?> visitor, Element element) {
		if (element.getName().equals("project"))
			return new Project(element);
		return null;
	}
	
	private Element _element;
	
	public Project(){
		
	}
	
	public Project(Element element){
		_element = element;
		//TODO: parse element here!
	}
	
}
