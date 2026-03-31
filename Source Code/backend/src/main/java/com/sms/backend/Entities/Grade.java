package com.sms.backend.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private ExamSchedule examId;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private Double marksObtained;
    private Double totalMarks;
    private String letterGrade;
    private String remarks;
}