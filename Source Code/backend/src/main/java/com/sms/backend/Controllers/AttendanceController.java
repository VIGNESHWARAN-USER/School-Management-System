package com.sms.backend.Controllers;

import java.util.List;

import com.sms.backend.Entities.StudentAttendance;
import com.sms.backend.Services.AttendanceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins="*")
public class AttendanceController {

    @Autowired
    private AttendanceServices attendanceService;

    @PostMapping("/save")
    public List<StudentAttendance> saveAttendance(@RequestBody List<StudentAttendance> attendanceList){
        return attendanceService.saveAttendance(attendanceList);
    }

    @GetMapping("/all")
    public List<StudentAttendance> getAllAttendance(){
        return attendanceService.getAllAttendance();
    }
}