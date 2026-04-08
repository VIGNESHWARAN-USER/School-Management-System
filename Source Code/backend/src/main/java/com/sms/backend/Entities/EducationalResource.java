package com.sms.backend.Entities;

import com.sms.backend.Enum.ResourceCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class EducationalResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String fileName;
    private String fileType;
    @Enumerated(EnumType.STRING)
    private ResourceCategory category;

    private LocalDateTime uploadDate;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "classId")
    private ClassRoom classRoom;
}