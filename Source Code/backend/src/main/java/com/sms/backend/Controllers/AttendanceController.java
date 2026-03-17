package com.sms.backend.Controllers;

import java.util.List;

import com.sms.backend.Entities.StudentAttendance;
import com.sms.backend.Entities.TeacherAttendance;
import com.sms.backend.Services.AttendanceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api")
public class AttendanceController {

    @Autowired
    private AttendanceServices attendanceService;

    @PostMapping("/markStudentAttendance")
    public List<StudentAttendance> saveStudentAttendance(@RequestBody List<StudentAttendance> attendanceList){
            return attendanceService.saveStudentAttendance(attendanceList);
    }

    @PostMapping("/markTeacherAttendance")
    public List<TeacherAttendance> saveTeacherAttendance(@RequestBody List<TeacherAttendance> attendanceList){
        return attendanceService.saveTeacherAttendance(attendanceList);
    }

//    @GetMapping("/getStudentAttendance")
//    public List<StudentAttendance> getAllAttendance(){
//        return attendanceService.getAllAttendance();
//    }
}