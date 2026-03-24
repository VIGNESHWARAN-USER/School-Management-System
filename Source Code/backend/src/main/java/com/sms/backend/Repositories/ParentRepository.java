package com.sms.backend.Repositories;
import com.sms.backend.Entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent,Long> {

    Parent findByEmail(String email);
}