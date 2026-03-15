package com.sms.backend.Entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProgressReport {

    @Id
    private Long reportId;

    private Long studentId;
    private int totalMarks;
    private double averageMarks;
    private String grade;

    @ElementCollection
    private List<String> gradeList;

    @ElementCollection
    private List<Integer> marksList;

    private String remarks;
}