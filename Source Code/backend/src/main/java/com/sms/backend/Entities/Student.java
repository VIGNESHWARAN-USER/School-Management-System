package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {

    @Id
    private Long studentId;

    private String studentName;
    private int age;
    private String email;
    private String password;
    private String classId;
    private String section;
    private Long parentId;
    private String address;
}