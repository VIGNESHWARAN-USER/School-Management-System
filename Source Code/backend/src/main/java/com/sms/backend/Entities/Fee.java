package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Fee {

    @Id
    private Long feeId;

    private Long studentId;
    private double totalAmount;
    private double paidAmount;
    private double pendingAmount;
    private String dueDate;
    private String status;   // Paid / Pending
}