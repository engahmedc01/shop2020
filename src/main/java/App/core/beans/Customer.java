package App.core.beans;


public class Customer  extends BaseBean{
	
	
	
	private int  id=-1;
	private String name;
	private String address;
	private String phone;
	private int  changed =0;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setChanged(int changed) {
		this.changed = changed;
	}
	
	
	

}
