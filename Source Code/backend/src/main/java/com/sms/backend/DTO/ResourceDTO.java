package com.sms.backend.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResourceDTO {

    private Long id;
    private String title;
    private String description;
    private String fileName;
    private String fileType;

    private String category;

    private LocalDateTime uploadDate;
    private String filePath;

    private String classId;
}