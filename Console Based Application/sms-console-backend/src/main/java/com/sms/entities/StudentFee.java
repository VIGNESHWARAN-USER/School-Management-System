package com.sms.entities;
//Author:Jothika R
/*
* This class is for the student fees
* OOPS:Encapsulation
*/
public class StudentFee {
    private long id;
    private long studentId;
    private long feeStructureId;
    private String status;
    private Long paymentId;

    //All arguments constructor
    public StudentFee(long id, long studentId, long feeStructureId, String status, Long paymentId) {
        this.id = id;
        this.studentId = studentId;
        this.feeStructureId = feeStructureId;
        this.status = status;
        this.paymentId = paymentId;
    }

    //Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getStudentId() { return studentId; }
    public void setStudentId(long studentId) { this.studentId = studentId; }
    public long getFeeStructureId() { return feeStructureId; }
    public void setFeeStructureId(long feeStructureId) { this.feeStructureId = feeStructureId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
}
