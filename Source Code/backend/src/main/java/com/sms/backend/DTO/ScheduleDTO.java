package com.sms.backend.DTO;

import lombok.Data;

@Data
public class ScheduleDTO {
    private Long classId;
    private Long subjectId;
    private Long teacherId;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
