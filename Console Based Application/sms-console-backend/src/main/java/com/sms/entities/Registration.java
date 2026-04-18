package com.sms.entities;
//Author:Shobana V
/*
* This class is to register the event
* OOPS:Encapsulation
*/
import java.time.LocalDate;

public class Registration {
	private long id;
    private long eventId;
    private long studentId;
    private LocalDate registrationDate;
	
    //All arguments constructor
	public Registration(long id, long eventId, long studentId, LocalDate registrationDate) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.studentId = studentId;
		this.registrationDate = registrationDate;
	}
	
    
    //Getters and Setters
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public long getStudentId() {
		return studentId;
	}
	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}
	public LocalDate getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

}
