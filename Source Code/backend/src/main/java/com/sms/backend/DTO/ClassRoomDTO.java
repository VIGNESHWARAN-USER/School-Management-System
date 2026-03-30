package com.sms.backend.DTO;

import lombok.Data;

@Data
public class ClassRoomDTO {
    private Long classId;
    private String academicYear;
    private int capacity;
    private String className;
    private String section;
    private Long teacherId;
}
