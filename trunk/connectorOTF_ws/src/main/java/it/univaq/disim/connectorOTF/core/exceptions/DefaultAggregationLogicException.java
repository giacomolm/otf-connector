package it.univaq.disim.connectorOTF.core.exceptions;

public class DefaultAggregationLogicException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DefaultAggregationLogicException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
		System.err.println(msg);
	}
	
	public  DefaultAggregationLogicException(Throwable cause) {
		super(cause);
	}
}
