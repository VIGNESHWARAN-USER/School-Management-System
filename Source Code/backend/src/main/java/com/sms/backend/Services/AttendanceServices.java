package com.sms.backend.Services;

import java.util.List;

import com.sms.backend.Entities.StudentAttendance;
import com.sms.backend.Repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AttendanceServices {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<StudentAttendance> saveAttendance(List<StudentAttendance> attendanceList){
        return attendanceRepository.saveAll(attendanceList);
    }

    public List<StudentAttendance> getAllAttendance(){
        return attendanceRepository.findAll();
    }
}