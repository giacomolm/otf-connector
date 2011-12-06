package core.compoundterm;

import java.util.Iterator;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

public class Plug extends CompoundTerm{

	CompoundTerm c1,c2;
	
	public Plug(CompoundTerm c1, CompoundTerm c2) {
		super(c1.getSources_uri(),c1.getReceivers_uri());
		/*for(Iterator<Port> i = c2.getSources_uri().iterator(); i.hasNext();){
			Port p = i.next();
			sources_uri.add(p);
		}*/
		addSources_uri(c2.getSources_uri());
		addReceivers_uri(c2.getReceivers_uri());
		//System.out.println(c1+" "+c1.getSources_uri());
		c1.getSources_uri().clear();
		c2.getSources_uri().clear();
		c1.getReceivers_uri().clear();
		c2.getReceivers_uri().clear();
		/*c1.getReceivers_uri().remove(c1.getReceivers_uri());
		c2.getSources_uri().remove(c2.getSources_uri());
		c2.getReceivers_uri().remove(c2.getReceivers_uri());*/
		//System.out.println("Ports: "+getSources_uri()+" "+c1.getSources_uri()+" "+c2.getSources_uri());
		this.c1 = c1;
		this.c2 = c2;
		//aggiungi tutte quante le route da porte esterne ad endpoint
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		c1.start();
		c2.start();
	}
	
	/*void addConnection(CompoundTerm c1, CompoundTerm c2){
		Endpoint local12 = context.getEndpoint("vm:local"+context.getUuidGenerator()+"12");
		Endpoint local21 = context.getEndpoint("vm:local"+context.getUuidGenerator()+"21");
		addInternalEndpoint(local12,local21);
	}*/

}
