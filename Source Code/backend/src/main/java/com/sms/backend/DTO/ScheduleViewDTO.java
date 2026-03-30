package com.sms.backend.DTO;

import lombok.Data;

@Data
public class ScheduleViewDTO {
    private String subjectName;
    private String teacherName;
    private String className;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}