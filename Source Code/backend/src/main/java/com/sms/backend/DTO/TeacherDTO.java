package com.sms.backend.DTO;

import com.sms.backend.Entities.Attendance;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {
    private Long id;
    private String name;
    private String subject;
    private String email;
    private String classId;
    private String phoneNumber;
    private List<AttendanceDTO> attendanceList;
}