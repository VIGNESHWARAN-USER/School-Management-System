package com.sms.backend.Repositories;

import com.sms.backend.Entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    //  Required (User Story 3)
    List<Grade> findByStudentId(Long studentId);

    //  Get all students marks for an exam
    List<Grade> findByExamId(Long examId);

    // Advanced filtering
    List<Grade> findByStudentIdAndExamId(Long studentId, Long examId);
}