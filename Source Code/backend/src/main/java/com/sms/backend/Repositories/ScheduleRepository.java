package com.sms.backend.Repositories;

import com.sms.backend.Entities.ClassRoom;
import com.sms.backend.Entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByClassRoom(ClassRoom classRoom);

    void deleteByClassRoom(ClassRoom classRoom);
}