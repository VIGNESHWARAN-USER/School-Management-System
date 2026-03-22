package com.sms.backend.Services;

import com.sms.backend.Entities.*;
import com.sms.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServices {

    @Autowired
    ExamRepository examRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    ProgressReportRepository progressReportRepository;

    @Autowired
    NotificationRepository notificationRepository;


    // ADD EXAM

    public String scheduleExam(Exam exam) {

        //  validation
        if (exam.getSubject() == null || exam.getExamDate() == null) {
            return "Subject and Date are required";
        }

        examRepository.save(exam);

        //  Send notification to parents
        Notification notification = new Notification();
        notification.setTitle("Exam Scheduled");
        notification.setMessage("New exam scheduled for " + exam.getSubject());
        notification.setStatus("Sent");

        notificationRepository.save(notification);

        return "Exam Scheduled Successfully";
    }


    // ADD GRADE

    public String addGrade(Grade grade) {

        //  validation
        if (grade.getMarksObtained() == 0) {
            return "Marks cannot be empty";
        }

        //  Grade Calculation
        if (grade.getMarksObtained() >= 90) {
            grade.setGrade("A");
        } else if (grade.getMarksObtained() >= 75) {
            grade.setGrade("B");
        } else if (grade.getMarksObtained() >= 50) {
            grade.setGrade("C");
        } else {
            grade.setGrade("Fail");
        }

        gradeRepository.save(grade);

        // Send notification
        Notification notification = new Notification();
        notification.setTitle("Results Published");
        notification.setMessage("Student results are published");
        notification.setStatus("Sent");

        notificationRepository.save(notification);

        return "Marks Added Successfully";
    }

    //  VIEW RESULTS

    public List<Grade> getStudentGrades(Long studentId, boolean isLoggedIn) {


        if (!isLoggedIn) {
            throw new RuntimeException("Unauthorized Access");
        }

        return gradeRepository.findByStudentId(studentId);
    }


    // PROGRESS REPORT

    public ProgressReport generateReport(Long studentId) {

        List<Grade> grades = gradeRepository.findByStudentId(studentId);

        int total = 0;

        for (Grade g : grades) {
            total += g.getMarksObtained();
        }

        double avg = grades.size() > 0 ? total / grades.size() : 0;

        ProgressReport report = new ProgressReport();
        report.setStudentId(studentId);
        report.setTotalMarks(total);
        report.setAverageMarks(avg);
        report.setRemarks("Generated Report");

        progressReportRepository.save(report);

        return report;
    }
}