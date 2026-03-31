package com.sms.backend.Controllers;

import com.sms.backend.DTO.ExamScheduleDTO;
import com.sms.backend.Entities.ExamSchedule;
import com.sms.backend.Entities.Grade;
import com.sms.backend.Services.ExamGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams-management")
@CrossOrigin(origins = "*")
public class ExamScheduleController {
    @Autowired
    private ExamGradeService service;


    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleExam(@RequestBody ExamScheduleDTO exam) {
        try {
            return ResponseEntity.ok(service.scheduleExam(exam));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/schedules")
    public List<ExamScheduleDTO> getSchedules() {
        return service.getAllExams();
    }

    @PostMapping("/grades")
    public ResponseEntity<?> saveGrade(@RequestBody Grade grade) {
        try {
            return ResponseEntity.ok(service.saveGrade(grade));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/results/{studentId}")
    public ResponseEntity<?> getResults(@PathVariable String studentId) {
        // AC2 logic would usually be handled by Spring Security filters,
        // but here is a manual check example:
        if (studentId == null || studentId.equals("null")) {
            return ResponseEntity.status(403).body("Access Denied: Please login.");
        }
        return ResponseEntity.ok(service.getStudentResults(studentId));
    }
}
