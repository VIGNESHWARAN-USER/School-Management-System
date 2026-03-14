package com.sms.backend.Repositories;

import com.sms.backend.Entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator,Long> {

    Administrator findByEmail(String email);
}