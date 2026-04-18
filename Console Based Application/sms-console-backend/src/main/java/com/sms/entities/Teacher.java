package com.sms.entities;

public class Teacher extends User {
    private String phoneNumber;
    private Long subjectId;
    private Long classId;
    
    public Teacher() 
    {
    	this.setRole("TEACHER"); 
    }
    
    
	public Teacher(Long id, String name, String email, String password, String role, String phoneNumber, Long subjectId,
			Long classId) {
		super(id, name, email, password, role);
		this.phoneNumber = phoneNumber;
		this.subjectId = subjectId;
		this.classId = classId;
	}



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