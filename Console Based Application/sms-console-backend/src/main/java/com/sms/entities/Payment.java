package com.sms.entities;

import java.time.LocalDateTime;

public abstract class Payment {
    private long id;
    private double amountPaid;
    private LocalDateTime paymentDate;
    private String status;
    private String remarks;
    private String paymentMethod;
}