package com.sms.entities;

public class Grade {
   
    private long id;
    private long studentId;
    private long examId;
    private double marksObtained;
    private String letterGrade;
    private String remarks;

    public Grade() {}

    public Grade(long id, long studentId, long examId, double marksObtained, String letterGrade, String remarks) {
        this.id = id;
        this.studentId = studentId;
        this.examId = examId;
        this.marksObtained = marksObtained;
        this.letterGrade = letterGrade;
        this.remarks = remarks;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getStudentId() { return studentId; }
    public void setStudentId(long studentId) { this.studentId = studentId; }
    public long getExamId() { return examId; }
    public void setExamId(long examId) { this.examId = examId; }
    public double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(double marksObtained) { this.marksObtained = marksObtained; }
    public String getLetterGrade() { return letterGrade; }
    public void setLetterGrade(String letterGrade) { this.letterGrade = letterGrade; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}