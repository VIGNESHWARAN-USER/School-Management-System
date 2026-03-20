package com.sms.backend.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentAttendance implements Comparable<StudentAttendance> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long classId;
    private LocalDate date;
    private String remarks;
    private String status;

    private String markedBy; // teacher name

    @Override
    public int compareTo(StudentAttendance o) {
        return this.date.compareTo(o.date);
    }
}