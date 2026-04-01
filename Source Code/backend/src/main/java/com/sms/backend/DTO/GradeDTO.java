package com.sms.backend.DTO;

import jakarta.persistence.*;
import lombok.Data;


@Data
public class GradeDTO {
    private Long studentId;
    private Long examId;
    private Double marksObtained;
    private Double totalMarks;
    private String letterGrade;
    private String remarks;
}