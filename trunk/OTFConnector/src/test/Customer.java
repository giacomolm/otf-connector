package test;
import java.util.ArrayList;
import java.util.List;

public class Customer {
	public int id;
	public String name;
	public List<Department> departments = new ArrayList<Department>();
	public Customer(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public void addDepartments(Department d){
		departments.add(d);
	}
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	public List<Department> getDepartments() {
		return departments;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
