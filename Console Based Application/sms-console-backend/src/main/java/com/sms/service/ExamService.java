package com.sms.service;

import java.util.List;
import com.sms.dao.ExamDAO;
import com.sms.dao.GradeDAO;
import com.sms.entities.Exam;
import com.sms.entities.Grade;

public class ExamService {

    private final ExamDAO examDAO = new ExamDAO();
    private final GradeDAO gradeDAO = new GradeDAO();

    public String addExam(Exam exam) {
        boolean success = examDAO.addExam(exam);
        return success ? "Exam added successfully!" : "Failed to add exam.";
    }

    public String updateExam(Exam exam) {
        boolean success = examDAO.updateExam(exam);
        return success ? "Exam updated successfully!" : "Failed to update exam.";
    }

    public String deleteExam(long examId) {
        boolean success = examDAO.deleteExam(examId);
        return success ? "Exam deleted successfully!" : "Failed to delete exam.";
    }

    public List<Exam> getAllExams() {
        return examDAO.getAllExams();
    }

    public List<Exam> getExamsByClass(long classId) {
        return examDAO.getExamsByClass(classId);
    }

    public Exam getExamById(long examId) {
        return examDAO.getExamById(examId);
    }

    public String assignGrade(Grade grade) {
        // Automatically calculate letter grade if not provided or to ensure consistency
        Exam exam = examDAO.getExamById(grade.getExamId());
        if (exam != null) {
            grade.setLetterGrade(calculateLetterGrade(grade.getMarksObtained(), exam.getMaxMarks()));
        }
        
        boolean success = gradeDAO.upsertGrade(grade);
        return success ? "Grade assigned successfully!" : "Failed to assign grade.";
    }

    public List<Grade> getStudentResults(long studentId) {
        return gradeDAO.getGradesByStudent(studentId);
    }

    public List<Grade> getExamResults(long examId) {
        return gradeDAO.getGradesByExam(examId);
    }

    public Grade getGradeForStudent(long studentId, long examId) {
        return gradeDAO.getGradeForStudent(studentId, examId);
    }

    private String calculateLetterGrade(double marks, double maxMarks) {
        double percentage = (marks / maxMarks) * 100;
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }
}
