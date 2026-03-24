package com.sms.backend.DTO;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class TransactionHistoryDTO {
    private String transactionId;
    private BigDecimal amountPaid;
    private String paymentDate;
    private String paymentMethod;
    private String paymentType; // ONLINE or OFFLINE
    private String status;
    private String installmentLabel; // e.g. "Term 1"
    private String remarks;
}