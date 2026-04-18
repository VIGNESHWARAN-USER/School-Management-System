package com.sms.entities;
//Author:Shobana V
/*
* This class is for assigning the subjects
* OOPS:Encapsulation
*/
public class Subject {
    private long subjectId;
    private String subjectName;
    private String subjectCode;
    private long classId;


    //No arguments constructor
    public Subject() {
    }

    //All arguments constructor

    public Subject(long subjectId, String subjectName, String subjectCode, long classId) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.classId = classId;
    }

    //Getters and setters
    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }
}