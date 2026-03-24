package com.sms.backend.DTO;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FeeDashboardDTO {
    private Long studentId;
    private String classId;
    private String academicYear;
    private BigDecimal amountPaid;
    private BigDecimal remainingBalance;
    private String overallStatus;
    private BigDecimal totalFeeWithTax;

    private List<ComponentDTO> breakdown;
    private List<InstallmentDTO> installments;
}