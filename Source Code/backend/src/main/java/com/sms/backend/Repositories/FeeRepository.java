package com.sms.backend.Repositories;

import com.sms.backend.Entities.Fee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    Fee findByStudentId(Long studentId);
}