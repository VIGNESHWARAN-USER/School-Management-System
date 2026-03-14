package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    private Long notificationId;

    private String title;
    private String message;
    private String date;
    private Long senderId;
    private Long receiverId;
    private String status; // Sent / Read
}