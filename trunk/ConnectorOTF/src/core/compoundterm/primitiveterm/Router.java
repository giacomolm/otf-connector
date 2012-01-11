package core.compoundterm.primitiveterm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import newTest.Department;

public class Router {

	Class methodclass;
	String methodname;
	ArrayList<String> receiver_uri;
	boolean invoked=false;
	static Object previous=null; 
	
	public Router(Class method_class, String method_name) {
		// TODO Auto-generated constructor stub
		methodclass = method_class;
		methodname = method_name;
	}
	//Questa funzione viene chiamata per ogni oggetto splittato
	public String route(Object body) throws IllegalArgumentException, InvocationTargetException{
		if(!body.equals(previous)){	
			previous = body;
			Method myMethod = null;
			Method[] m = methodclass.getDeclaredMethods();
			for(int i=0; i<m.length; i++){
				if(m[i].getName().equals(methodname)){
					myMethod = m[i];
				}
			}
			try {
				Object t = methodclass.newInstance();
				Collection<String> effective_receiver = (Collection) myMethod.invoke(t,body);
				if(!effective_receiver.isEmpty()){
					Iterator<String> i = effective_receiver.iterator();
					String result = i.next();
					while(i.hasNext()){
						String s = i.next();
						result+=","+s;
					}
					//resitutisco gli uri destinatari per quel particolare oggetto splittato separati da una virgola
					return result;
				}
						
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		//invoca la mia logica di routing
		}
		else return null;
	}
}
