package com.sms.entities;

public class Administrator extends User {
    
	public Administrator() 
    {
    	this.setRole("ADMIN"); 
    }

	public Administrator(Long id, String name, String email, String password, String role) {
		super(id, name, email, password, role);
	}
	
	
}