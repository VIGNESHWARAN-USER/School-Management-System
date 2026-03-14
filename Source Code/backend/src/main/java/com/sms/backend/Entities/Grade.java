package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grade {

    @Id
    private Long gradeId;

    private Long studentId;
    private Long examId;
    private int marksObtained;
    private String grade;
    private String remarks;

}