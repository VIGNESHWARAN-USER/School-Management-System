package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Resource {

    @Id
    private Long resourceId;

    private String title;
    private String description;
    private String resourceType; // PDF / Video / Assignment
    private String uploadDate;

    private Long teacherId;
    private String classId;
}