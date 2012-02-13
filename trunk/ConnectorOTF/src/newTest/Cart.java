package newTest;
import java.util.ArrayList;


public class Cart {
	
	int id;
	ArrayList<Item> al = new ArrayList<Item>();
	
	public Cart(int id){
		this.id = id;
	}
	
	public Cart(int id, Item i) {
		// TODO Auto-generated constructor stub
		this.id = id;
		al.add(i);
	}
	
	public void addItem(Item i){
		al.add(i);
	}

	public ArrayList<Item> getItems(){
		return al;
	}
}
