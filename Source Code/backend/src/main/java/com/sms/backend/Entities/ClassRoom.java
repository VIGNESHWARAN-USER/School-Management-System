package com.sms.backend.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    private String className;
    private String section;
    private int capacity;

    @OneToMany(mappedBy = "classRoom")
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "classRoom")
    private List<Student> students;

    @OneToMany(mappedBy = "classRoom")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "classRoom")
    private List<ExamSchedule> examSchedules;

    @OneToOne
    @JoinColumn(name = "fee_structure_id")
    private FeeStructure feeStructure;

    private String academicYear;
}