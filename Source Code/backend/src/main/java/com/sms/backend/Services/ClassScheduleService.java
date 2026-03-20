package com.sms.backend.Services;

import com.sms.backend.Entities.ClassRoom;
import com.sms.backend.Entities.Subject;
import com.sms.backend.Entities.Schedule;
import com.sms.backend.Repositories.ClassRoomRepository;
import com.sms.backend.Repositories.SubjectRepository;
import com.sms.backend.Repositories.ScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassScheduleService {

    @Autowired
    ClassRoomRepository classRoomRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    ScheduleRepository scheduleRepository;


    // 1️⃣ Add Classroom
    public String addClassRoom(ClassRoom classRoom)
    {
        classRoomRepository.save(classRoom);
        return "ClassRoom Added Successfully";
    }


    // 2️⃣ Add Subject
    public String addSubject(Subject subject)
    {
        subjectRepository.save(subject);
        return "Subject Added Successfully";
    }


    // 3️⃣ Add Schedule
    public String addSchedule(Schedule schedule)
    {
        scheduleRepository.save(schedule);
        return "Schedule Added Successfully";
    }
}