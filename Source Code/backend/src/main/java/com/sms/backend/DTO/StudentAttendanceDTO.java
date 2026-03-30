package com.sms.backend.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentAttendanceDTO {
    private String classId;
    private LocalDate date;
    private String remarks;
    private String status;
}
