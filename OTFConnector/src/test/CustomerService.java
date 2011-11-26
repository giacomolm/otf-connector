package test;

import java.util.List;

public class CustomerService {
	public List<Department> splitDepartments(Customer customer) {
		return customer.getDepartments();
	}
	public Department setPlace(Department d){
		d.name = "Dipartimento di "+d.name;
		return d;
	}
}
