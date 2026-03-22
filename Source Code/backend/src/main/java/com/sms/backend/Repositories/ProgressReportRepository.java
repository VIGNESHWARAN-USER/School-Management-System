package com.sms.backend.Repositories;

import com.sms.backend.Entities.ProgressReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressReportRepository extends JpaRepository<ProgressReport, Long> {

    //  Fetch report for a student
    ProgressReport findByStudentId(Long studentId);
}