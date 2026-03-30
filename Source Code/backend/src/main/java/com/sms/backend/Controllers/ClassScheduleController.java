package com.sms.backend.Controllers;

import com.sms.backend.DTO.ClassRoomDTO;
import com.sms.backend.DTO.ScheduleDTO;
import com.sms.backend.Entities.ClassRoom;
import com.sms.backend.Entities.Subject;
import com.sms.backend.Entities.Schedule;
import com.sms.backend.Repositories.ClassRoomRepository;
import com.sms.backend.Services.ClassScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ClassScheduleController {

    @Autowired
    ClassScheduleService classScheduleService;


    @GetMapping("/classrooms")
    public ResponseEntity<?> getClassRooms()
    {
        return classScheduleService.getAllClassRooms();
    }

    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjects()
    {
        return classScheduleService.getAllSubjects();
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachers()
    {
        return classScheduleService.getAllTeachers();
    }



    @PostMapping("/addClassRoom")
    public ResponseEntity<?> addClassRoom(@RequestBody ClassRoomDTO classRoom)
    {
        return classScheduleService.addClassRoom(classRoom);
    }



    @PostMapping("/addSubject")
    public String addSubject(@RequestBody Subject subject)
    {
        return classScheduleService.addSubject(subject);
    }



    @PostMapping("/addSchedule")
    public String addSchedule(@RequestBody ScheduleDTO schedule)
    {
        return classScheduleService.addSchedule(schedule);
    }

    @GetMapping("fetchClassSchedule/{classId}")
    public ResponseEntity<?> fetchClassSchedule(@PathVariable Long classId)
    {
        return classScheduleService.fetchClassSchedule(classId);
    }

    @GetMapping("/api/fetchStudentSchedule/{userId}")
    public ResponseEntity<?> fetchStudentSchedule(@PathVariable Long userId)
    {
        return classScheduleService.fetchStudentSchedule(userId);
    }

    @GetMapping("/api/fetchTeacherSchedule/{userId}")
    public ResponseEntity<?> fetchTeacherSchedule(@PathVariable Long userId)
    {
        return classScheduleService.fetchTeacherSchedule(userId);
    }
}
