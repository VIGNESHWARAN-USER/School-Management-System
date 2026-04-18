package com.sms.entities;
//Author:Vigneshwaran M
/*
* This is an inherited class of an user 
* OOPS:Inheritance
*/
public class Administrator extends User {
    //No arguments constructor
	public Administrator() 
    {
    	this.setRole("ADMIN"); 
    }
	
    //All arguments constructor

	public Administrator(Long id, String name, String email, String password, String role) {
		super(id, name, email, password, role);
	}
	
	
}