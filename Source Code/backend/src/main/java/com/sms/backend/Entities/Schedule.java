package com.sms.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule {

    @Id
    private Long scheduleId;

    private Long classId;
    private String subject;
    private Long teacherId;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}