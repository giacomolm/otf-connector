package it.univaq.disim.connectorOTF.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import it.univaq.disim.connectorOTF.core.compoundterm.CompoundTerm;

/**
 * Port class model concept of message port introduced in Connector Algebra
 * theory. From this theory we know that each term is charactered by his input/output message port. 
 * Each port contains informations about managed uri. Uri identifies terms associated to this term:
 * if term considers this port such an input port, the term consume from this uri;
 * while if this term considers this port such an output port, we send the output
 * exchange to the indicated uri. 
 * Ports determine behaviour of the terms. A the term building time, we must define 
 * also types allowed to pass through this port: in fact, if a term receive massage 
 * of type different from expected, term doesn't manage input message. Each term owning 
 * this port is identified by his id (added at building time)
 * Port class must contemplate composition of term: if components terms share 
 * the same input port (or output port), port must add both terms internally.
 * @author giacomolm
 *
 */

public class Port {
	
	String uri;
	ArrayList<Class> types = new ArrayList<Class>();
	ArrayList<CompoundTerm> terms = new ArrayList<CompoundTerm>();
	ArrayList<Integer> id = new ArrayList<Integer>();
	
	/**
	 * Constructs new Port starting from an existing port
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
	 * Constructs new port passing base information 
	 * @param uri associated to this port
	 * @param list of type of objects passing through this port
	 * @param id of the component that we are building  
	 */
	public Port(String uri,Collection<Class> types_mess,int id) {
		this.uri = uri;
		Iterator<Class> i = types_mess.iterator();
		while(i.hasNext()){
			Class type = i.next();
			types.add(type);
			this.id.add(Integer.valueOf(id));
		}
	}	

	/**
	 * Get uri associated to this port
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
	 * Get type allowed to pass through this port, defined by terms
	 * owning this port
	 * @return Collection of type allowed to pass through this port
	 */
	public ArrayList<Class> getType() {
		return types;
	}

	/** 
	 * Get terms associated to this port
	 * @return Collection of compound term that own this port 
	 */
	public ArrayList<CompoundTerm> getTerms() {
		return terms;
	}

	/**
	 * Set term owning this port
	 * @param term owning port
	 */
	public void setTerm(CompoundTerm term) {
		terms.clear();
		terms.add(term);
	}

	/**
	 * Get the id collections of terms owning this port
	 * @return Collection of terms
	 */
	public ArrayList<Integer> getId() {
		return id;
	}

	/**
	 * Adds the id associated to the term adding the port
	 * @param id of term
	 */
	public void addId(int id) {
		this.id.add(new Integer(id));
	}
	
	/**
	 * Add port p information to this port. It allows adding information
	 * between terms sharing the same input port or output port 
	 * @param p Port
	 */
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
