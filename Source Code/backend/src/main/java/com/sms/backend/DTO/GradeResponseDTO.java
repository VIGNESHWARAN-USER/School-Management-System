package com.sms.backend.DTO;

import lombok.Data;

@Data
public class GradeResponseDTO {
    private Double marksObtained;
    private Double totalMarks;
    private String gradeLetter;
    private String subjectName;
    private String subjectId;
    private String remarks;
    private String academicYear;
}
