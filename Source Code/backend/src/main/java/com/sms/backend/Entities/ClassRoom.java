package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ClassRoom {

    @Id
    private Long classId;

    private String className;
    private String section;
    private int capacity;
    private Long teacherId;
    private String academicYear;
}