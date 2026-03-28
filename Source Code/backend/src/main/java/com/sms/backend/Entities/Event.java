package com.sms.backend.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    private List<Registration> registrationList;
}