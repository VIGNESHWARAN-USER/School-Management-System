package com.sms.backend.Repositories;

import com.sms.backend.Entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // 1️⃣ Fetch participation of specific student/teacher
    List<Registration> findByParticipantId(Long participantId);
    List<Registration> findByEventId(Long eventId);


    // 2️⃣ Prevent duplicate registration
    boolean existsByEventIdAndParticipantId(Long eventId, Long participantId);
}