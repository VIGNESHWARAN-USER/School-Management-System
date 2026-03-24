package com.sms.backend.Repositories;

import com.sms.backend.Entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    //  Get exams by class
    List<Exam> findByClassId(String classId);

    //  Get exams by subject
    List<Exam> findBySubject(String subject);
}