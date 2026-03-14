package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    private Long messageId;

    private Long senderId;
    private Long receiverId;
    private String messageText;
    private String date;
    private String status; // Sent / Seen
}