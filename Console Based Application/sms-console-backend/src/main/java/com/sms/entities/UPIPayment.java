package com.sms.entities;

/**
 * UPI Payment — validates UPI ID format (e.g. name@bank).
 * Simulates processing via a mock gateway.
 */
public class UPIPayment extends Payment {

    private String upiId;

    public UPIPayment(double amountPaid, String upiId) {
        super(amountPaid, "UPI");
        this.upiId = upiId;
    }

    public String getUpiId() { return upiId; }

    @Override
    public String validate() {
        if (upiId == null || upiId.trim().isEmpty()) {
            return "UPI ID cannot be empty.";
        }
        // Must match pattern: localpart@bankcode  e.g. john@okicici, 9876543210@ybl
        if (!upiId.matches("^[a-zA-Z0-9.\\-_]{3,}@[a-zA-Z]{3,}$")) {
            return "Invalid UPI ID format. Expected format: user@bank (e.g. john@okicici)";
        }
        return null;
    }

    @Override
    public boolean process() {
        System.out.println("  [UPI] Sending payment request to VPA: " + upiId + " ...");
        simulateGatewayDelay();
        System.out.println("  [UPI] Transaction approved by gateway.");
        setStatus("SUCCESS");
        setRemarks("UPI payment via " + upiId);
        return true;
    }

    private void simulateGatewayDelay() {
        try { Thread.sleep(1200); } catch (InterruptedException ignored) {}
    }
}
