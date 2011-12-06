package core;

import core.compoundterm.CompoundTerm;

public class Port {
	
	String uri;
	Class type;
	CompoundTerm term;
	int id;
	
	public Port(){
		
	}
	
	public Port(String uri,Class type,int id) {
		this.uri = uri;
		this.type = type;
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public CompoundTerm getTerm() {
		return term;
	}

	public void setTerm(CompoundTerm term) {
		this.term = term;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
