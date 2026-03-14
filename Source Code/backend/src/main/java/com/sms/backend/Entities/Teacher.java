package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Teacher {

    @Id
    private Long teacherId;

    private String teacherName;
    private String subject;
    private String email;
    private String password;
    private String phoneNumber;
}