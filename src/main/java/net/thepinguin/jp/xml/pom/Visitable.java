package net.thepinguin.jp.xml.pom;

import java.util.List;

import net.thepinguin.jp.xml.base.Element;

public interface Visitable<T> {
	
	public Visitable<T> isTypeOf(Visitable<?> visitor, Element e);
//	public abstract IVisitable isTypeOf(IVisitable visitor, Element e);
	
}
