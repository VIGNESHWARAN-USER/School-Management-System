package com.sms.backend.Repositories;

import com.sms.backend.Entities.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    ClassRoom findByClassId(Long classId);

    void deleteByClassId(Long classId);
}