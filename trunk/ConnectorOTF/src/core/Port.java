package core;

import java.util.ArrayList;

import core.compoundterm.CompoundTerm;

public class Port {
	
	String uri;
	ArrayList<Class> types = new ArrayList<Class>();
	ArrayList<CompoundTerm> terms = new ArrayList<CompoundTerm>();
	ArrayList<Integer> id = new ArrayList<Integer>();
	
	/**
	 * Constructs new Port from an existing port
	 * @param p existing port  
	 */
	public Port(Port p){
		uri = p.getUri();
		types.addAll(p.getType());
	}	
	
	/**
	 * Constructs new port passing base information 
	 * @param uri associated to this port
	 * @param type of the object that pass through this port
	 * @param id of the component that we are building  
	 */
	public Port(String uri,Class type,int id) {
		this.uri = uri;
		types.add(type);
		this.id.add(Integer.valueOf(id));
	}

	/**
	 * @return uri associated to this port
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Set uri of this port
	 * @param uri of the port
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return Collection of type allowed to pass through this port
	 */
	public ArrayList<Class> getType() {
		return types;
	}

	/** 
	 * @return Collection of compound term that own this port 
	 */
	public ArrayList<CompoundTerm> getTerms() {
		return terms;
	}

	/**
	 * Set term owning this port
	 * @param term
	 */
	public void setTerm(CompoundTerm term) {
		terms.clear();
		terms.add(term);
	}

	public ArrayList<Integer> getId() {
		return id;
	}

	public void addId(int id) {
		this.id.add(new Integer(id));
	}
	
	public void add(Port p){
		types.addAll(p.getType());
		id.addAll(p.getId());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Port "+getUri()+" Terms"+ getTerms();
	}
}
