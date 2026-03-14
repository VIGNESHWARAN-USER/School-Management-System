package com.sms.backend.Repositories;

import com.sms.backend.Entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
}