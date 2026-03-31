package com.sms.backend.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    private String subjectName;

    @Column(unique = true)
    private String subjectCode;

    @OneToMany(mappedBy = "subject")
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "subject")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "subject")
    private List<ExamSchedule> examSchedules;
}