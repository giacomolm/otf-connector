package core.compoundterm.primitiveterm;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import core.Port;

/**
 * Trans class implements signature mismatch primitive. There are occasions when 
 * a message to be exchanged between two components is functionally compatible 
 * yet syntactically inconsistent. Such a mismatch may be resolved by means of 
 * a translating primitive Trans. 
 * The signature mismatch primitive is semantically identical to message 
 * translator pattern included in EIP. Apache Camel supports the Message 
 * Translator from the EIP patterns by using an arbitrary Processor in the 
 * routing logic: we simply use the DSL to 
 * User of this class defines transformation logic:
 * in the definition of trans term, user specify how input message must be 
 * translated to be compatible with receiver; hence, user must define new method
 * having as parameter the expected message and as body the manipulation of his
 * content.
 * @author giacomolm
 *
 */

public class Trans extends PrimitiveTerm{

	Class methodclass;
	String methodname;
	String receiver;
	Port receiver_port,source_port;
	
	/**
	 * Build new trans term, including base info such as source and receiver uri,
	 * and types expected from these uri.
	 * @param sourceUri term consumes message from this sourceUri
	 * @param in_type term is able to manage this type of input-message
	 * @param receiverUri term sends message to this receiverUri
	 * @param out_type type of the output message
	 */
	public Trans(final String sourceUri, Class in_type, final String receiverUri, Class out_type){
		source_port = new Port(sourceUri,in_type,getId());
		addSource(source_port);
		receiver_port = new Port(receiverUri,out_type,getId());
		addReceiver(receiver_port);
		
		System.out.println("Component "+this+" added, source: "+sourceUri+" to: "+receiverUri);
	}
	
	/**
	 * This version of constructor specify also information about method that 
	 * manipulates source message
	 * @param sourceUri term consumes message from this sourceUri
	 * @param in_type term is able to manage this type of input-message
	 * @param receiverUri term sends message to this receiverUri
	 * @param out_type type of the output message
	 * @param method_class Class of the method that manipulates input message
	 * @param method_name Name of the method manipulating input message
	 */
	public Trans(final String sourceUri, Class in_type,final String receiverUri, Class out_type,Class method_class, String method_name){
		source_port = new Port(sourceUri,in_type,getId());
		addSource(source_port);
		receiver_port = new Port(receiverUri,out_type,getId());
		addReceiver(receiver_port);
		setTransformLogic(method_class, method_name);
		System.out.println("Component "+this+" added, source: "+sourceUri+" to: "+receiverUri);
	}
	
	/**
	 * Set information of the class manipulating input message
	 * @param method_class Class of the method that manipulates input message
	 * @param method_name Name (String) of the method manipulating input message
	 */
	public void setTransformLogic(Class method_class, String method_name){
		methodclass = method_class;
		methodname = method_name;
	}
	
	/**
	 * Starts context associated the trans term. It simply add new route: consumes
	 * message from internal endpoint, transforms the message deleting incompatibility
	 * and sends the output message to receivers uri. 
	 */
	@Override
	public void start() {
		super.start();
		try {
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from(internal+""+getId()).
					transform().method(methodclass, methodname).
					to(receiver_port.getUri());
				}
			});
			System.out.println("Component "+this+" started source(s) "+getSources()+" "+getId()+" receiver(s)"+getReceivers());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the receiver of output message
	 * @param receiver Endpoint receiving output message
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * Set the Exchange e as input message of this trans term. This term
	 * check the equality of the uri received as parameter and source uri:
	 * if these two uri are the same, the method is forwarded to internal
	 * endpoint
	 */
	@Override
	public void setMessage(String uri, Exchange e) {
		// TODO Auto-generated method stub
		if(source_port.getUri().equals(uri)){
			producer.send(internal+""+source_port.getId().get(0), e);
		}
	}
}
