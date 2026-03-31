package com.sms.backend.Repositories;

import com.sms.backend.Entities.EducationalResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<EducationalResource, Long> {
}