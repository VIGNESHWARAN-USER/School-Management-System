package com.sms.entities;

import java.time.LocalDateTime;

public abstract class Payment {
    private long id;
    private double amountPaid;
    private LocalDateTime paymentDate;
    private String status;
    private String remarks;
    private String paymentMethod;

    public Payment(double amountPaid, String paymentMethod) {
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    /** Validate payment-specific inputs. Returns error message or null if valid. */
    public abstract String validate();

    /** Process the payment after validation. Returns true on success. */
    public abstract boolean process();

    // ─── Getters & Setters ───
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}