package com.sms.backend.Repositories;

import com.sms.backend.Entities.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<ExamSchedule, Long> {}

