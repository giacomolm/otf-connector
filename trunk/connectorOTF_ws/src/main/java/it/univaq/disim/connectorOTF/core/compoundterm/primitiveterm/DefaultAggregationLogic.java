package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import it.univaq.disim.connectorOTF.core.exceptions.DefaultAggregationLogicException;

/**
 * Follow the details listed in http://camel.apache.org/aggregator.html
 * @author giacomolm
 *
 */

public class DefaultAggregationLogic implements AggregationStrategy {

	Class out_class;
	
	public DefaultAggregationLogic() {
		// TODO Auto-generated constructor stub
		out_class = Collection.class;
	}
	
	public DefaultAggregationLogic(Class out) throws DefaultAggregationLogicException{
		// TODO Auto-generated constructor stub
		if(!out.equals(ArrayList.class)) throw new DefaultAggregationLogicException("Cannot use DefaultAggregation logic: receiver endpoint doesn't exeptect message of type ArrayList.class, but message of type "+out);
		out_class = out;
	}
	
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Object newBody = newExchange.getIn().getBody();
		//System.out.println("body "+newBody);
		ArrayList<Object> list = null;
		//first time we do aggregation
	    if (oldExchange == null) {
			list = new ArrayList<Object>();
			list.add(newBody);
			newExchange.getIn().setBody(list);
			return newExchange;
	    } 
	    else{
		    list = oldExchange.getIn().getBody(ArrayList.class);
			list.add(newBody);
			return oldExchange;
	    }
    }
}
