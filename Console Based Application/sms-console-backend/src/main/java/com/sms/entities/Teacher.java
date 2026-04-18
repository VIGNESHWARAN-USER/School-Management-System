package com.sms.entities;
//Author:Vigneshwaran M
/*
* This class is an inherited class from user
* OOPS:Inheritance,Encapsulation
*/
public class Teacher extends User {
    private String phoneNumber;
    private Long subjectId;
    private Long classId;

    //No arguments constructor
    public Teacher() 
    {
    	this.setRole("TEACHER"); 
    }
    

    //All arguments constructor
	public Teacher(Long id, String name, String email, String password, String role, String phoneNumber, Long subjectId,
			Long classId) {
		super(id, name, email, password, role);
		this.phoneNumber = phoneNumber;
		this.subjectId = subjectId;
		this.classId = classId;
	}


//Getters and Setters
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}
    
    
}