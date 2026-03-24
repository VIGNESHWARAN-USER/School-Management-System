package com.sms.backend.DTO;

import lombok.Data;

import java.util.List;

@Data
public class InstallmentPlanDTO {
    private Long feeStructureId;
    private List<InstallmentItemDTO> installments;
}