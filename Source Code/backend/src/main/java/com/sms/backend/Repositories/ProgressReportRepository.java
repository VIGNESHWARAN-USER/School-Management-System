package com.sms.backend.Repositories;

import com.sms.backend.Entities.ProgressReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressReportRepository extends JpaRepository<ProgressReport, Long> {
}