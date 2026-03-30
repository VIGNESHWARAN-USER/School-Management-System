package com.sms.backend.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TeacherAttendanceDTO {
    private Long id;
    private Long classId;
    private String remarks;
    private String status;
    private LocalDate date;
}
