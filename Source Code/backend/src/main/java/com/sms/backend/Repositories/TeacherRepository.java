package com.sms.backend.Repositories;

import com.sms.backend.Entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    Teacher findByEmail(String email);
}