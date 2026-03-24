package com.sms.backend.Repositories;

import com.sms.backend.Entities.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {

    FeeStructure findByClassIdAndAcademicYear(String classId, String academicYear);
}