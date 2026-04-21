package com.sms.entities;

//Author:Vigneshwaran M
/*
 * This is an abstract class of an user 
 * classes like admin,teacher,student,parent are inherited from here
 * OOPS:Encapsulation,Abstraction
 */
public abstract class User {
	private Long id;
	private String name;
	private String email;
	private String password;
	private String role;

	// All arguments constructor
	public User(Long id, String name, String email, String password, String role) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	// No arguments constructor
	public User() {
	}

	// Getters and setter
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

}