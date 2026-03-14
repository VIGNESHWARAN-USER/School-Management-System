package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {

    @Id
    private Long paymentId;

    private Long studentId;
    private double amount;
    private String paymentDate;
    private String paymentMethod;  // Cash / Card / UPI
    private String transactionId;
}