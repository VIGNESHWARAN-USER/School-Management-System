package com.sms.backend.Entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Data
public class ExamSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;


    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomNumber;
    private LocalDateTime publishedAt;
}

    