package com.sms.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Exam {
    private long id;
    private long subjectId;
    private long classRoomId;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomNumber;
}