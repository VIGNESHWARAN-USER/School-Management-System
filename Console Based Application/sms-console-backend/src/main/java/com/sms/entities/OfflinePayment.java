package com.sms.entities;

/**
 * Offline / Cash Payment — collects receipt reference and cash amount tendered.
 */
public class OfflinePayment extends Payment {

    private String receiptReference;
    private double cashTendered;

    public OfflinePayment(double amountPaid, String receiptReference, double cashTendered) {
        super(amountPaid, "OFFLINE");
        this.receiptReference = receiptReference.trim();
        this.cashTendered = cashTendered;
    }

    public String getReceiptReference() { return receiptReference; }
    public double getCashTendered() { return cashTendered; }

    @Override
    public String validate() {
        if (receiptReference == null || receiptReference.length() < 3) {
            return "Receipt reference must be at least 3 characters.";
        }
        if (cashTendered < getAmountPaid()) {
            return String.format("Cash tendered (%.2f) is less than the amount due (%.2f).", cashTendered, getAmountPaid());
        }
        return null;
    }

    @Override
    public boolean process() {
        double change = cashTendered - getAmountPaid();
        System.out.printf("  [OFFLINE] Cash accepted. Change to return: %.2f%n", change);
        System.out.println("  [OFFLINE] Receipt reference: " + receiptReference);
        setStatus("SUCCESS");
        setRemarks("Offline cash payment — ref: " + receiptReference);
        return true;
    }
}
