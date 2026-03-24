package com.sms.backend.DTO;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class InstallmentDTO {
    private Long id;
    private String name;
    private BigDecimal amount;
    private String dueDate;
    private String status;
}
