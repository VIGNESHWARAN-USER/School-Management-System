package com.sms.backend.Controllers;

import java.time.LocalDate;
import java.util.List;

import com.sms.backend.DTO.AttendanceDTO;
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
    public ResponseEntity<?> saveStudentAttendance(@RequestBody List<AttendanceDTO> attendanceList){
            return attendanceService.saveStudentAttendance(attendanceList);
    }

    @PostMapping("/markTeacherAttendance")
    public ResponseEntity<?> saveTeacherAttendance(@RequestBody List<AttendanceDTO> attendanceList){
        return attendanceService.saveTeacherAttendance(attendanceList);
    }

    @GetMapping("/attendance/class/{classId}/date/{selected_date}")
    public ResponseEntity<?> getAllAttendance(@PathVariable int classId, @PathVariable LocalDate selected_date){
        return attendanceService.getAllStudentAttendance(classId, selected_date);
    }
}