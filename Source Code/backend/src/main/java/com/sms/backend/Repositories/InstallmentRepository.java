package com.sms.backend.Repositories;

import com.sms.backend.Entities.Installment;
import com.sms.backend.Entities.StudentFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    void deleteByStudentFee(StudentFee studentFee);
}
