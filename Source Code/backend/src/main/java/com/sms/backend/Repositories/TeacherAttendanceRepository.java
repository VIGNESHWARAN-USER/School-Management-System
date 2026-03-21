package com.sms.backend.Repositories;

import com.sms.backend.Entities.StudentAttendance;
import com.sms.backend.Entities.Teacher;
import com.sms.backend.Entities.TeacherAttendance;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TeacherAttendanceRepository extends JpaRepository<TeacherAttendance, Long> {
    TeacherAttendance findByDateAndTeacher(LocalDate date, Teacher teacher);

    List<TeacherAttendance> findAllByDate(LocalDate date);
}