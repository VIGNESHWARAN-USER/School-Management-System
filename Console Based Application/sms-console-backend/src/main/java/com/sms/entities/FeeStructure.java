package com.sms.entities;

public class FeeStructure {
    private long id;
    private long classRoomId;
    private double totalAmount;
    private String description;
    private String term;

    public FeeStructure() {
    }

    public FeeStructure(long id, long classRoomId, double totalAmount, String description, String term) {
        this.id = id;
        this.classRoomId = classRoomId;
        this.totalAmount = totalAmount;
        this.description = description;
        this.term = term;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(long classRoomId) {
        this.classRoomId = classRoomId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
