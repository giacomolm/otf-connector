package test;

import org.apache.camel.Exchange;
import org.apache.camel.StreamCache;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.strategy.GenericFileRenameProcessStrategy;
import org.apache.camel.component.seda.SedaConsumer;

public class Handler{
	public String setContent(String body){
		/*String content = e.getIn().getBody(String.class);
		content+=" Modificato";
		e.getIn().setBody(content);*/
		System.out.println(body);
		return "Giacomo";
	}
	
	public Customer addLastName(Customer c){
		c.name += " Lamonaco";
		return c;
	}
}