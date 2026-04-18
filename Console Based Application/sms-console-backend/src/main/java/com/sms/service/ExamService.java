package com.sms.service;

// Author: Jothika R
// This class handles exam and grade related operations
import java.util.List;
import com.sms.dao.ExamDAO;
import com.sms.dao.GradeDAO;
import com.sms.entities.Exam;
import com.sms.entities.Grade;

public class ExamService {

    // DAO objects to interact with database
    private final ExamDAO examDAO = new ExamDAO();
    private final GradeDAO gradeDAO = new GradeDAO();

    // Add a new exam
    public String addExam(Exam exam) {
        boolean success = examDAO.addExam(exam);
        return success ? "Exam added successfully!" : "Failed to add exam.";
    }

    // Update existing exam details
    public String updateExam(Exam exam) {
        boolean success = examDAO.updateExam(exam);
        return success ? "Exam updated successfully!" : "Failed to update exam.";
    }

    // Delete exam using examId
    public String deleteExam(long examId) {
        boolean success = examDAO.deleteExam(examId);
        return success ? "Exam deleted successfully!" : "Failed to delete exam.";
    }

    // Get all exams
    public List<Exam> getAllExams() {
        return examDAO.getAllExams();
    }

    public List<Exam> getExamsByClass(long classId) { // Collections used
        return examDAO.getExamsByClass(classId);
    }

    // Get exam details by examId
    public Exam getExamById(long examId) {
        return examDAO.getExamById(examId);
    }

    public String assignGrade(Grade grade) { 
    	
        Exam exam = examDAO.getExamById(grade.getExamId());

        // Calculate grade based on marks
        if (exam != null) {
            grade.setLetterGrade(
                calculateLetterGrade(grade.getMarksObtained(), exam.getMaxMarks())
            );
        }

        boolean success = gradeDAO.upsertGrade(grade);
        return success ? "Grade assigned successfully!" : "Failed to assign grade.";
    }

    public List<Grade> getStudentResults(long studentId) { // Collections used
        return gradeDAO.getGradesByStudent(studentId);
    }

    // Collection used
    public List<Grade> getExamResults(long examId) {
        return gradeDAO.getGradesByExam(examId);
    }

    public Grade getGradeForStudent(long studentId, long examId) { // Type Conversion  
        return gradeDAO.getGradeForStudent(studentId, examId);
    }

    // Convert marks into letter grade
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