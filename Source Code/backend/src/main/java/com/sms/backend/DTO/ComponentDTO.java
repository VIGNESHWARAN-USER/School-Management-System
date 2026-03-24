package com.sms.backend.DTO;

import lombok.Data;


@Data
public class ComponentDTO {
    private String name;
    private String description;
    private Double amount;
    private Double taxPercentage;
}