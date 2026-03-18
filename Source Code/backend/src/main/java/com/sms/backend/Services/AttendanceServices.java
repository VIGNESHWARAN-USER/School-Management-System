package com.sms.backend.Services;

import java.util.List;

import com.sms.backend.Entities.StudentAttendance;
import com.sms.backend.Entities.TeacherAttendance;
import com.sms.backend.Repositories.AttendanceRepository;
import com.sms.backend.Repositories.StudentAttendanceRepository;
import com.sms.backend.Repositories.TeacherAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AttendanceServices {

    @Autowired
    private StudentAttendanceRepository studentAttendanceRepository;
    @Autowired
    private TeacherAttendanceRepository teacherAttendanceRepository;

    public List<StudentAttendance> saveStudentAttendance(List<StudentAttendance> attendanceList){
        try
        {
            return studentAttendanceRepository.saveAll(attendanceList);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<TeacherAttendance> saveTeacherAttendance(List<TeacherAttendance> attendanceList){
        return teacherAttendanceRepository.saveAll(attendanceList);
    }

    public List<StudentAttendance> getAllStudentAttendance(){
        return studentAttendanceRepository.findAll();
    }
}