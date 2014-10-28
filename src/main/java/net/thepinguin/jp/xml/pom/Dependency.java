package net.thepinguin.jp.xml.pom;

import net.thepinguin.jp.xml.base.*;

public class Dependency implements Visitable<Dependency> {

	private Element _element;
	private String _groupId = "";
	private String _artifactId = "";
	private String _version = "";
	private String _scope = "";

	public Dependency() {

	}

	public Dependency(Element element) {
		_element = element;
		// map nested elements to placeholders
		for (Element e : _element.getElements()) {
			if (e.getName().equals("groupId"))
				_groupId = e.getValue();
			else if (e.getName().equals("artifactId"))
				_artifactId = e.getValue();
			else if (e.getName().equals("version"))
				_version = e.getValue();
			else if (e.getName().equals("scope"))
				_scope = e.getValue();
		}
	}

	public Visitable<Dependency> isTypeOf(Visitable<?> visitor, Element element) {
		if (element.getName().equals("dependency"))
			return new Dependency(element);
		return null;
	}

	public String getGroupId() {
		return _groupId;
	}

	public String getArtifactId() {
		return _artifactId;
	}

	public String getVersion() {
		return _version;
	}

	public String getScope() {
		return _scope;
	}

}
