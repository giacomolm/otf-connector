package core;

import java.util.ArrayList;

import core.compoundterm.CompoundTerm;

public class Port {
	
	String uri;
	ArrayList<Class> types = new ArrayList<Class>();
	ArrayList<CompoundTerm> terms = new ArrayList<CompoundTerm>();
	ArrayList<Integer> id = new ArrayList<Integer>();
	
	public Port(){
		
	}
	
	public Port(Port p){
		uri = p.getUri();
		types.addAll(p.getType());
	}	
	
	public Port(String uri,Class type,int id) {
		this.uri = uri;
		types.add(type);
		this.id.add(Integer.valueOf(id));
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public ArrayList<Class> getType() {
		return types;
	}

	public ArrayList<CompoundTerm> getTerms() {
		return terms;
	}

	public void setTerm(CompoundTerm term) {
		terms.clear();
		terms.add(term);
	}

	public ArrayList<Integer> getId() {
		return id;
	}

	/*public void setId(int id) {
		this.id = id;
	}*/

}
