package com.sms.backend.Repositories;

import com.sms.backend.Entities.FeeStructure;
import com.sms.backend.Entities.StudentFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentFeeRepository extends JpaRepository<StudentFee, Long> {


    StudentFee findByStudentId(Long studentId);

    List<StudentFee> findAllByFeeStructure(FeeStructure feeStructure);
}
