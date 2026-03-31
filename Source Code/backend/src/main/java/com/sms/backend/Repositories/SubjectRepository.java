package com.sms.backend.Repositories;

import com.sms.backend.Entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Subject findBySubjectCode(String subjectCode);

    Subject findBySubjectId(Long subjectId);
}