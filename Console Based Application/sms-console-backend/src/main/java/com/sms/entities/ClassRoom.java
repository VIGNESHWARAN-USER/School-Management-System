package com.sms.entities;


public class ClassRoom {    
    private long id;
    private String className;
    private String section;
    private int capacity;
    private String academicYear;

    public ClassRoom() {
    }

    public ClassRoom(long id, String className, String section, int capacity, String academicYear) {
        this.id = id;
        this.className = className;
        this.section = section;
        this.capacity = capacity;
        this.academicYear = academicYear;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
}