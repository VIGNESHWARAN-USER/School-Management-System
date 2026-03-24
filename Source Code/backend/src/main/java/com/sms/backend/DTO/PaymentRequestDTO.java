package com.sms.backend.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    private Long studentId;
    private Long installmentId;
    private BigDecimal amount;
    private String paymentMethod; // UPI, CARD, etc.
}