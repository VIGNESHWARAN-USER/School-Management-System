package com.sms.entities;
//Author:Vigneshwaran M

/*
* This class is an inherited class from user 
* OOPS:Inheritance, Encapsulation
*/

public class Student extends User {
	private int age;
	private String address;
	private String parentEmail;
	private long classId;

	// No arguments constructor
	public Student() {
		this.setRole("STUDENT");
	}

	// All arguments constructor
	public Student(Long id, String name, String email, String password, String role, int age, String address,
			String parentEmail, long classId) {
		super(id, name, email, password, role);
		this.age = age;
		this.address = address;
		this.parentEmail = parentEmail;
		this.classId = classId;
	}

//Getters and Setters
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