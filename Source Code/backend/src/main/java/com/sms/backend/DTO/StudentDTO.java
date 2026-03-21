package com.sms.backend.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    private Long id;
    private String name;
    private int age;
    private String email;
    private String classId;
    private String address;

    private ParentDTO parentDTO;
    private List<AttendanceDTO> attendanceDTOList;
}