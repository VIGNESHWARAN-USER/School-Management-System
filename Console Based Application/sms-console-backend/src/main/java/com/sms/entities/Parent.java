package com.sms.entities;

public class Parent extends User {
    private String mobileNumber;
    private String address;
    private int age;
    
    public Parent() 
    { 
    	this.setRole("PARENT");
    }
    
	public Parent(Long id, String name, String email, String password, String role, String mobileNumber, String address,
			int age) {
		super(id, name, email, password, role);
		this.mobileNumber = mobileNumber;
		this.address = address;
		this.age = age;
	}



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
    
    
    
}