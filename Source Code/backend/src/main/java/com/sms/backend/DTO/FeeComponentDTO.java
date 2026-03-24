package com.sms.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeeComponentDTO {
    private String componentName;
    private String description;
    private Double amount;
    private Double taxPercentage;
}