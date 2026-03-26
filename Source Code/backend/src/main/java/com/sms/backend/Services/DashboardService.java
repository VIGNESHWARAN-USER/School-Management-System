package com.sms.backend.Services;

import com.sms.backend.Entities.StudentFee;
import com.sms.backend.Repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    ClassRoomRepository classRoomRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    StudentFeeRepository feeRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    StudentAttendanceRepository attendanceRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    StudentFeeRepository studentFeeRepository;


    //  MAIN METHOD (ROLE BASED)
    public Map<String, Object> getDashboard(String role, Long userId)
    {
        switch (role.toLowerCase())
        {
            case "admin":
                return getAdminDashboard();

            case "teacher":
                return getTeacherDashboard();

            case "student":
                return getStudentDashboard(userId);

            case "parent":
                return getParentDashboard(userId);

            default:
                return Map.of("message", "Invalid Role");
        }
    }

    //  ADMIN
    private Map<String, Object> getAdminDashboard()
    {
        Map<String, Object> data = new HashMap<>();

        data.put("totalStudents", studentRepository.count());
        data.put("totalTeachers", teacherRepository.count());
        data.put("totalParents", parentRepository.count());
        data.put("totalClasses", classRoomRepository.count());
        data.put("totalEvents", eventRepository.count());

        double collected = 0;
        double pending = 0;

        for(StudentFee fee : feeRepository.findAll())
        {
            collected += Double.parseDouble(String.valueOf(fee.getAmountPaid()));
            pending += Double.parseDouble(String.valueOf(fee.getRemainingBalance()));
        }

        data.put("totalFeeCollected", collected);
        data.put("totalFeePending", pending);

        data.put("attendanceCount", attendanceRepository.count());

        return data;
    }

    // TEACHER
    private Map<String, Object> getTeacherDashboard()
    {
        Map<String, Object> data = new HashMap<>();

        data.put("schedule", scheduleRepository.findAll());
        data.put("totalStudents", studentRepository.count());
        data.put("attendanceCount", attendanceRepository.count());

        return data;
    }

    //  STUDENT
    private Map<String, Object> getStudentDashboard(Long studentId)
    {
        Map<String, Object> data = new HashMap<>();

        data.put("attendance", attendanceRepository.findAll());
        data.put("grades", gradeRepository.findAll());
        data.put("schedule", scheduleRepository.findAll());
        data.put("fee", feeRepository.findByStudentId(studentId));

        return data;
    }

    //  PARENT
    private Map<String, Object> getParentDashboard(Long studentId)
    {
        Map<String, Object> data = new HashMap<>();

        data.put("student", studentRepository.findById(studentId).orElse(null));
        data.put("attendance", attendanceRepository.findAll());
        data.put("grades", gradeRepository.findAll());
        data.put("fee", feeRepository.findByStudentId(studentId));
        data.put("payments", studentFeeRepository.findByStudentId(studentId));

        return data;
    }
}