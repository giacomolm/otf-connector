package core.compoundterm.primitiveterm;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class DefaultAggregationLogic implements AggregationStrategy {

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Object newBody = newExchange.getIn().getBody();
		ArrayList<Object> list = null;
	    if (oldExchange == null) {
			list = new ArrayList<Object>();
			list.add(newBody);
			newExchange.getIn().setBody(list);
			return newExchange;
	    } else {
		    list = oldExchange.getIn().getBody(ArrayList.class);
			list.add(newBody);
			return oldExchange;
	    }
    }
}
