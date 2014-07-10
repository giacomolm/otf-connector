/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.core.exceptions;

/**
 *
 * @author Giacomo
 */
public class DefaultIllegalStateException extends Exception{

	private static final long serialVersionUID = 1L;

	public DefaultIllegalStateException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
		System.err.println(msg);
	}
	
	public  DefaultIllegalStateException(Throwable cause) {
		super(cause);
	}
}
