package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Submission {

    @Id
    private Long submissionId;

    private Long assignmentId;
    private Long studentId;

    private String submissionDate;
    private String filePath;
    private String grade;
}