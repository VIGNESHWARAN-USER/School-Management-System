package com.sms.backend.Repositories;

import com.sms.backend.Entities.TeacherAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherAttendanceRepository extends JpaRepository<TeacherAttendance, Long> {
}