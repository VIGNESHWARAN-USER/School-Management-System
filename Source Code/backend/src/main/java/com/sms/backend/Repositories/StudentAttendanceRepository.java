package com.sms.backend.Repositories;

import com.sms.backend.Entities.Student;
import com.sms.backend.Entities.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Long> {
    StudentAttendance findByDateAndStudent(LocalDate date, Student student);

    List<StudentAttendance> findAllByClassId(int classId);

    List<StudentAttendance> findAllByClassIdAndDate(int classId, LocalDate date);
}