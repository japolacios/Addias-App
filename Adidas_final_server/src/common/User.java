package common;

import java.io.Serializable;


public class User implements Serializable {

	
	private static final long serialVersionUID = 1L;
	String name, msg;
	int id;
	

	public User(String nombre, String _msg, int id) {
		
		this.name = nombre;
		msg = _msg;
		this.id=id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getMsg(){
		return msg;
	}
	
	
}
