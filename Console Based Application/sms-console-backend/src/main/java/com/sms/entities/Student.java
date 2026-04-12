package com.sms.entities;


public class Student extends User {
    private int age;
    private String address;
    private String parentEmail;
    private long classId;
    
    public Student() {
    	this.setRole("STUDENT"); 	
    }
    
    

	public Student(Long id, String name, String email, String password, String role, int age, String address,
			String parentEmail, long classId) {
		super(id, name, email, password, role);
		this.age = age;
		this.address = address;
		this.parentEmail = parentEmail;
		this.classId = classId;
	}



	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getParentEmail() {
		return parentEmail;
	}

	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}
    
}