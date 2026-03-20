package com.sms.backend.Controllers;

import java.util.List;

import com.sms.backend.Entities.StudentAttendance;
import com.sms.backend.Entities.TeacherAttendance;
import com.sms.backend.Services.AttendanceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api")
public class AttendanceController {

    @Autowired
    private AttendanceServices attendanceService;

    @PostMapping("/markStudentAttendance")
    public ResponseEntity<?> saveStudentAttendance(@RequestBody List<StudentAttendance> attendanceList){
            return attendanceService.saveStudentAttendance(attendanceList);
    }

    @PostMapping("/markTeacherAttendance")
    public ResponseEntity<?> saveTeacherAttendance(@RequestBody List<TeacherAttendance> attendanceList){
        return attendanceService.saveTeacherAttendance(attendanceList);
    }

    @GetMapping("/fetchAllStudentsAttendannce/{classId}")
    public ResponseEntity<?> getAllAttendance(@PathVariable Long classId){
        return attendanceService.getAllStudentAttendance(classId);
    }
}