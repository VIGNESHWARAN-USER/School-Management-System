package com.sms.backend.Repositories;

import com.sms.backend.Entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Subject findBySubjectCode(String subjectCode);

    Subject findBySubjectId(Long subjectId);
}