package com.sms.backend.DTO;

import lombok.Data;

@Data
public class InstallmentItemDTO {
    private String installmentName;
    private String dueDate;
    private Double percentage; // percentage of total fee

}