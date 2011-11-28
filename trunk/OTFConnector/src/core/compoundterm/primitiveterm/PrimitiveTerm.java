package core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.camel.builder.RouteBuilder;

import core.compoundterm.CompoundTerm;

public abstract class PrimitiveTerm extends CompoundTerm {

	protected RouteBuilder primitive_route;
	
	/*public PrimitiveTerm(String source_uri){
		this.source_uri = source_uri;
	}*/
	
	public PrimitiveTerm(String source_uri, String receiver_uri){
		super(source_uri,receiver_uri);
	}
	
	public PrimitiveTerm(String source_uri, Collection<String> receivers_uri){
		super(source_uri,receivers_uri);
	}
	
	PrimitiveTerm(Collection<String> sources_uri, Collection<String> receivers_uri){
		super(sources_uri,receivers_uri);
	}

	public RouteBuilder getRoute() {
		return primitive_route;
	}

	public void setRoute(RouteBuilder primitiveroute) {
		this.primitive_route = primitiveroute;
	}
	
	//Hanno visibilità di package in quanto l'utente del sistema non deve sapere come manipoliamo i nostri receiver
	
	void setReceivers_uri(ArrayList<String> receiversUri) {
		receivers_uri = receiversUri;
	}
	
}
