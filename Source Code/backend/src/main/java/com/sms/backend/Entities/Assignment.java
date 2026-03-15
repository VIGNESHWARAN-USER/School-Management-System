package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Assignment {

    @Id
    private Long assignmentId;

    private String title;
    private String description;
    private String dueDate;

    private Long teacherId;
    private String classId;
}