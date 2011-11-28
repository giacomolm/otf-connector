package core.compoundterm;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public abstract class CompoundTerm {

	Collection<String> sources_uri;
	protected Collection<String> receivers_uri;
	protected static CamelContext context= new DefaultCamelContext();
	
	public CompoundTerm(String sourceuri, String receiveruri) {
		sources_uri = new ArrayList<String>();
		receivers_uri = new ArrayList<String>();
		sources_uri.add(sourceuri);
		receivers_uri.add(receiveruri);
	}
	
	public CompoundTerm(String sourceuri, Collection<String> receiversuri) {
		sources_uri = new ArrayList<String>();
		receivers_uri = new ArrayList<String>();
	}
	
	public CompoundTerm(Collection<String> sourcesuri, Collection<String> receiversuri) {
		sources_uri = sourcesuri;
		receivers_uri = receiversuri;
	}

	public Collection<String> getSources_uri() {
		return sources_uri;
	}

	public void setSources_uri(Collection<String> sourcesUri) {
		sources_uri = sourcesUri;
	}
	
	public void addSources_uri(Collection<String> sourcesUri){
		sources_uri.addAll(sourcesUri);
	}

	public Collection<String> getReceivers_uri() {
		return receivers_uri;
	}

	public void setReceivers_uri(Collection<String> receiversUri) {
		receivers_uri = receiversUri;
	}
	
	public void addReceivers_uri(Collection<String> receiversUri){
		receivers_uri.addAll(receiversUri);
	}
}
