package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Registration {

    @Id
    private Long registrationId;

    private Long eventId;
    private Long participantId;
    private String participantType;
    private String registrationDate;
    private String status;
}