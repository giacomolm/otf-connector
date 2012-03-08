/**
 * Rappresenta lo stesso esempio del tutorial, soltanto che questa volta non usiamo
 * cart e item ma Customer e Department.
 */

package newTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import newTest.Customer;
import newTest.Department;
import core.compoundterm.CompoundTerm;
import core.compoundterm.Plug;
import core.compoundterm.primitiveterm.Cons;
import core.compoundterm.primitiveterm.Prod;
import core.compoundterm.primitiveterm.Split;

public class SplitterExample {
	
	public static void main(String[] args) {
		Customer customer = new Customer(1,"Giacomo");
		Department d1 =  new Department(1, "acquisti");
		Department d2 =  new Department(2, "marketing");
		customer.addDepartments(d1);
		customer.addDepartments(d2);
		Department[] a = new Department[10];
		a[0] = new Department(1, "acquisti");
		a[1] = new Department(2, "marketing");
		Split s = new Split("vm:start", Customer.class, "vm:endpoint1,vm:endpoint2,vm:endpoint3",Department.class);
		//s.setSplittingLogic(Third.class, "splitDepartments");
		
		
		CompoundTerm c = new Plug(new Plug(new Prod("vm:start",Department[].class,a), s),new Cons("vm:endpoint2",Department.class));
		//CompoundTerm c = new Plug(new Plug(new Prod("vm:start",Customer.class,customer), s),new Cons("vm:endpoint2",Department.class));
		c.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Department> splitDepartments(Customer customer) {
		return customer.getDepartments();
	}
	
	/*viene invocato per ogni oggetto splittato
	 *Using a Pojo to do the splitting:
	 *As the Splitter can use any Expression to do the actual splitting we leverage this fact and use a method expression to invoke a Bean to get the splitted parts.
	 *The Bean should return a value that is iterable such as: java.util.Collection, java.util.Iterator or an array.
	 */
	public Collection<String> routing(Department body){
		Collection<String> dest = new ArrayList<String>();
		if (body.name.equals("acquisti")){
			dest.add("vm:endpoint2");
			return dest;
		}
		else return null;
	}
}
