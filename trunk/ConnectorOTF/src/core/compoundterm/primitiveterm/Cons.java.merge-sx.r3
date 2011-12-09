package test;


import core.Connector_old;

public class HttpTest {

	public static void main(String[] args) {
		Connector_old c = new Connector_old();
		c.addSource("http://localhost");
		c.addTransformer(String.class, HttpTest.class, "addTag");
		c.addReceiver("file://test");
		c.start(5000);
	}
	
	public String addTag(String source){
		if (source!=null && source.equals(old)) return null;
		else {
			old = source;
			return source+="<br><br><h1>Cameled</h1>";
		}
		
		
	}

	private static String old = null;
}
