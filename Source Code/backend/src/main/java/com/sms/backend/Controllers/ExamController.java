package com.sms.backend.Controllers;

import com.sms.backend.Entities.*;
import com.sms.backend.Services.ExamServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    ExamServices service;


    // USER STORY 1

    @PostMapping("/schedule")
    public String scheduleExam(@RequestBody Exam exam) {
        return service.scheduleExam(exam);
    }


    // USER STORY 2

    @PostMapping("/addGrade")
    public String addGrade(@RequestBody Grade grade) {
        return service.addGrade(grade);
    }


    // USER STORY 3

    @GetMapping("/grades")
    public List<Grade> getGrades(@RequestParam Long studentId,
                                 @RequestParam boolean isLoggedIn) {
        return service.getStudentGrades(studentId, isLoggedIn);
    }


    // PROGRESS REPORT

    @GetMapping("/report")
    public ProgressReport getReport(@RequestParam Long studentId) {
        return service.generateReport(studentId);
    }
}