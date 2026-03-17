package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import java.util.List;
import java.util.PriorityQueue;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String email;
    private String password;
    private String classId;
    private Long parentId;
    private String address;
    private PriorityQueue<StudentAttendance> attendence;
}