package com.sms.entities;

import java.util.ArrayList;
import java.util.List;

//Author:Vigneshwaran M
/*
 * This class is an inherited class from user
 * OOPS:Encapsulation,Inheritance
 */
public class Parent extends User {
    private String mobileNumber;
    private String address;
    private int age;
    private List<Long> childIds = new ArrayList<>();//Collections
    
	//No arguments constructor

    public Parent() 
    { 
    	this.setRole("PARENT");
    }
    

    
    //All arguments constructor
	public Parent(Long id, String name, String email, String password, String role, String mobileNumber, String address,
			int age, List<Long> childIds) //Collections
	{
		super(id, name, email, password, role);
		this.mobileNumber = mobileNumber;
		this.address = address;
		this.age = age;
		if (childIds != null) this.childIds = childIds;
	}


	//All arguments constructor
	public Parent(Long id, String name, String email, String password, String role, String mobileNumber, String address,
			int age) {
		super(id, name, email, password, role);
		this.mobileNumber = mobileNumber;
		this.address = address;
		this.age = age;
	}

	//Getters and Setters
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
    
    public java.util.List<Long> getChildIds() {
        return childIds;
    }
    
    public void setChildIds(List<Long> childIds) {
        this.childIds = childIds;
    }
    

}