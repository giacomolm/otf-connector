package it.univaq.disim.connectorOTF.core.compoundterm.primitiveterm;

import static it.univaq.disim.connectorOTF.core.compoundterm.CompoundTerm.fstream;
import static it.univaq.disim.connectorOTF.core.compoundterm.CompoundTerm.out;
import it.univaq.disim.connectorOTF.core.exceptions.DefaultTransformLogicException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultTransformLogic {
	
	static Class out_type;
	
        FileOutputStream fstream;
	static PrintWriter out = null;
	
	public DefaultTransformLogic() {
		// TODO Auto-generated constructor stub
		this.out_type = String.class;
                
                
            try {                
                fstream = new FileOutputStream("/usr/share/tomcat7/bin/log.txt",true);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DefaultTransformLogic.class.getName()).log(Level.SEVERE, null, ex);
	}
		out = new PrintWriter(fstream,true);
	}
	
	public DefaultTransformLogic(Class out_type) {
		// TODO Auto-generated constructor stub
		this.out_type = out_type;
	}
	
	public static Object transform(Object o) throws DefaultTransformLogicException{
                System.out.println("Consuming object "+o);
		if(o.getClass().equals(out_type))
			return o;
		else throw new DefaultTransformLogicException("DefaultTransformLogic cannot transform incoming message. Type expected by the receiver is "+out_type+" and is different respect "+o);
	}

}
