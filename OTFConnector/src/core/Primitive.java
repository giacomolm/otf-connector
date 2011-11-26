package core;

import java.util.ArrayList;

import org.apache.camel.builder.RouteBuilder;

public abstract class Primitive {

	protected RouteBuilder myroute;
	protected String source_uri;
	protected ArrayList<String> receivers_uri;
	
	public Primitive(){	}
	
	public Primitive(String source_uri){
		this.source_uri = source_uri;
	}
	
	/*public Primitive(ArrayList<String> receiversUri){
		receivers_uri=receiversUri;
	}*/
	
	public Primitive(String source_uri, String receiver_uri){
		this.source_uri = source_uri;
		setReceiver_uri(receiver_uri);
	}
	
	public Primitive(String source_uri, ArrayList<String> receivers_uri){
		this.source_uri = source_uri;
		this.receivers_uri = receivers_uri;
	}

	public String getSource_uri() {
		return source_uri;
	}

	public void setSource_uri(String sourceUri) {
		source_uri = sourceUri;
	}

	public ArrayList<String> getReceivers_uri() {
		return receivers_uri;
	}

	public void setReceivers_uri(ArrayList<String> receiversUri) {
		receivers_uri = receiversUri;
	}
	
	public void setReceiver_uri(String receiverUri){
		receivers_uri = new ArrayList<String>();
		receivers_uri.add(receiverUri);
	}
	
	public String getReceiver_uri(){
		return receivers_uri.get(0);
	}

	public RouteBuilder getRoute() {
		return myroute;
	}

	public void setRoute(RouteBuilder myroute) {
		this.myroute = myroute;
	}
}
