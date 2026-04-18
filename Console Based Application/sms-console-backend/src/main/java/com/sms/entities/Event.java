package com.sms.entities;
//Author:Shobana V
/*
* This class for managing the events
* OOPS:Encapsulation
*/
public class Event {

    private Long id;
    private String eventName;
    private String eventDescription;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String organizer;
    private int maxParticipants;
    private int currentParticipants;
    private String eventStatus;

    //No arguments constructor 
    public Event() {}


    //All arguments constructor 
    public Event(Long id, String eventName, String eventDescription, String eventDate,
                 String eventTime, String eventLocation, String organizer,
                 int maxParticipants, int currentParticipants, String eventStatus) {
        this.id = id;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.organizer = organizer;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.eventStatus = eventStatus;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventDescription() { return eventDescription; }
    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }
    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }
    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }
    public int getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(int currentParticipants) { this.currentParticipants = currentParticipants; }
    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }
}