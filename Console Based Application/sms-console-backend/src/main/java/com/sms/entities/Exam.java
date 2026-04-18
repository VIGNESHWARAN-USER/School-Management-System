package com.sms.entities;

public class Exam {
    private long id;
    private String name;
    private String description;
    private long subjectId;
    private long classRoomId;
    private String examDate;   // YYYY-MM-DD
    private double maxMarks;

    public Exam() {}

    public Exam(long id, String name, String description, long subjectId, long classRoomId,
                String examDate, double maxMarks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subjectId = subjectId;
        this.classRoomId = classRoomId;
        this.examDate = examDate;
        this.maxMarks = maxMarks;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getSubjectId() { return subjectId; }
    public void setSubjectId(long subjectId) { this.subjectId = subjectId; }
    public long getClassRoomId() { return classRoomId; }
    public void setClassRoomId(long classRoomId) { this.classRoomId = classRoomId; }
    public String getExamDate() { return examDate; }
    public void setExamDate(String examDate) { this.examDate = examDate; }
    public double getMaxMarks() { return maxMarks; }
    public void setMaxMarks(double maxMarks) { this.maxMarks = maxMarks; }
}