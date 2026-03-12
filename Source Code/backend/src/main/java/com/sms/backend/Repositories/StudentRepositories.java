package com.sms.backend.Repositories;

import com.sms.backend.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepositories extends JpaRepository<Student, Long> {

}
