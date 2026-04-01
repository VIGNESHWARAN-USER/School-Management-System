package com.sms.backend.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeeStructureDTO {
    private Long feeStructureId;
    private String className;
    private String classId;
    private String academicYear;
    private List<FeeComponentDTO> components;
    private BigDecimal totalAmount;
}