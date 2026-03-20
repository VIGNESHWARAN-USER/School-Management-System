package com.sms.backend.Repositories;

import com.sms.backend.Entities.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Long> {
    StudentAttendance findByDateAndMemberId(LocalDate date, Long memberId);

    List<StudentAttendance> findAllByClassId(Long classId);
}