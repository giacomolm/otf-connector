package core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Router {

	Class methodclass;
	String methodname;
	ArrayList<String> receiver_uri;
	boolean invoked=false;
	
	public Router(Class method_class, String method_name) {
		// TODO Auto-generated constructor stub
		methodclass = method_class;
		methodname = method_name;
	}
	
	public String route(String body) throws IllegalArgumentException, InvocationTargetException{
		Method myMethod = null;
		Method[] m = methodclass.getDeclaredMethods();
		for(int i=0; i<m.length; i++){
			if(m[i].getName().equals(methodname)){
				myMethod = m[i];
			}
		}
		try {
			Object t = methodclass.newInstance();
			Collection<String> effective_receiver = (Collection) myMethod.invoke(t,body,receiver_uri);
			
			if(!effective_receiver.isEmpty()){
				Iterator<String> i = effective_receiver.iterator();
				String result = i.next();
				while(i.hasNext()){
					String s = i.next();
					result+=","+s;
				}
				if(!invoked) {
					invoked = true;
					return result;
				}
				else {
					invoked = false;
					return null;
				}
			}
					
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//invoca la mia logica di routing

		return null;
	}
}
