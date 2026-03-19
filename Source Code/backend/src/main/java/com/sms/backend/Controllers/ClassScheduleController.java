package com.sms.backend.Controllers;

import com.sms.backend.Entities.ClassRoom;
import com.sms.backend.Entities.Subject;
import com.sms.backend.Entities.Schedule;
import com.sms.backend.Services.ClassScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/class")
public class ClassScheduleController {

    @Autowired
    ClassScheduleService classScheduleService;


    // 1️⃣ Add Classroom
    @PostMapping("/addClassRoom")
    public String addClassRoom(@RequestBody ClassRoom classRoom)
    {
        return classScheduleService.addClassRoom(classRoom);
    }


    // 2️⃣ Add Subject
    @PostMapping("/addSubject")
    public String addSubject(@RequestBody Subject subject)
    {
        return classScheduleService.addSubject(subject);
    }


    // 3️⃣ Add Schedule
    @PostMapping("/addSchedule")
    public String addSchedule(@RequestBody Schedule schedule)
    {
        return classScheduleService.addSchedule(schedule);
    }

}