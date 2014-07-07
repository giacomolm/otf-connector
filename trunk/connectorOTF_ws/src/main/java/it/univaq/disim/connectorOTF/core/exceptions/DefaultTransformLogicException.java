package it.univaq.disim.connectorOTF.core.exceptions;

public class DefaultTransformLogicException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DefaultTransformLogicException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
		System.err.println(msg);
	}
	
	public  DefaultTransformLogicException(Throwable cause) {
		super(cause);
	}
}
