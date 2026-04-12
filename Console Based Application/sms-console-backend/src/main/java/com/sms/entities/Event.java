package com.sms.entities;

import java.util.List;

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
    private List<Long> registration_ids;
}