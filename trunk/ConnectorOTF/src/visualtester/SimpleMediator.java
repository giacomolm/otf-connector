package visualtester;

import java.util.ArrayList;

import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Merge;
import core.compoundterm.primitiveterm.Order;
import core.compoundterm.primitiveterm.Split;
import core.compoundterm.primitiveterm.Trans;


public class SimpleMediator {
	public static void main(String[] args){
		//PER IL TRANSLATOR
		Trans t = new Trans("mina:tcp://localhost:6886?textline=true&sync=false", String.class, "file://log?fileName=report.txt", String.class);
		t.setTransformLogic(SimpleMediator.class, "transMethod");
		t.start();
		
		//PER LO SPLITTER
		Split split = new Split("mina:tcp://localhost:6887?textline=true&sync=false", String.class, "file://log?fileName=report.txt", ArrayList.class);
		split.setSplittingLogic(SimpleMediator.class, "splitMethod");
		split.start();
		
		//PER IL MERGE
		Split split2 = new Split("mina:tcp://localhost:6894?textline=true&sync=false", String.class, "vm:porta1", String.class);
		split2.setSplittingLogic(SimpleMediator.class, "splitMethod2");
		
	    Merge m = new Merge("vm:porta1", String.class, "vm:porta2", ArrayList.class);
		m.setCompletitionSize(4);
		
		Trans t2 = new Trans("vm:porta2", ArrayList.class,"file://log?fileName=report.txt", String.class);
		t2.setTransformLogic(SimpleMediator.class, "transMethod2");
		
		CompoundTerm ct = new Plug(t2,new Plug(m,split2));
		ct.start();
		
		//PER L'ORDER
		ArrayList<Class> in_types = new ArrayList<Class>();
		in_types.add(String.class);
		ArrayList<Class> out_types = new ArrayList<Class>();
		out_types.add(String.class);
		Split split3 = new Split("mina:tcp://localhost:6889?textline=true&sync=false", String.class, "vm:door1", String.class);
		split3.setSplittingLogic(SimpleMediator.class, "splitMethod2");
		//split3.start();
		Order o = new Order("vm:door1", in_types, "vm:door2", out_types);
		o.setSequenceSize(5);
		o.setPermutation(new SimpleMediator(), "orderMethod");
		//o.start();
		
		Merge merge2 = new Merge("vm:door2", String.class, "vm:door3", ArrayList.class);
		merge2.setCompletitionSize(5);
		//merge2.start();
		
		Trans t3 = new Trans("vm:door3", String.class, "file://log?fileName=report.txt", String.class);
		t3.setTransformLogic(SimpleMediator.class, "transMethod2");
		//t3.start();
		
		CompoundTerm ct2 = new Plug(t3,new Plug(merge2,(new Plug(o,split3))));
		ct2.start();
	}
	
	public String transMethod(String s){
		System.out.println(s);
		if(s.equals("Ciao")) return "Hello";
		else return "No in Dict";
	}
	
	public String transMethod2(ArrayList<String> s){
		return s.toString();
	}
	
	/*public String transMethod2(String s){
		System.out.println("trans2 "+s);
		return s;
	}*/
	
	public ArrayList<String> splitMethod(String s){
		String[] as = s.split(" ");
		ArrayList<String> al = new ArrayList<String>();
		for(int i=0; i<as.length; i++){
			al.add(as[i]);
			//System.out.println("aggiunto "+ as[i]);
		}
		return al;
	}
	
	public ArrayList<String> splitMethod2(String s){
		String[] as = s.split(",");
		ArrayList<String> al = new ArrayList<String>();
		for(int i=0; i<as.length; i++){
			al.add(as[i]);
		}
		return al;
	}
	
	public Integer orderMethod(Object num){
		/*if(num.equals("0")) return new Integer(0);
		else if(num.equals("1")) return new Integer(1);
		else if(num.equals("2")) return new Integer(2);
		else if(num.equals("3")) return new Integer(3);
		else if(num.equals("4")) return new Integer(4);
		else if(num.equals("5")) return new Integer(5);
		else {
			System.out.println("meno1");
			new Integer(-1);
		}*/
		Integer i = Integer.parseInt(((String) num));
		if(i.intValue()>=0) return i;
		else return -1;
		
	}
}
