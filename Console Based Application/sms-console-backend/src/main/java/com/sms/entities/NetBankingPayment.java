package com.sms.entities;

/**
 * Net Banking Payment — validates account number (9-18 digits), IFSC code (e.g. SBIN0001234).
 */
public class NetBankingPayment extends Payment {

    private String bankName;
    private String accountNumber;
    private String ifscCode;

    public NetBankingPayment(double amountPaid, String bankName, String accountNumber, String ifscCode) {
        super(amountPaid, "NET_BANKING");
        this.bankName = bankName;
        this.accountNumber = accountNumber.trim();
        this.ifscCode = ifscCode.trim().toUpperCase();
    }

    public String getBankName() { return bankName; }
    public String getAccountNumber() { return accountNumber; }
    public String getIfscCode() { return ifscCode; }

    @Override
    public String validate() {
        if (bankName == null || bankName.trim().length() < 2) {
            return "Bank name must be at least 2 characters.";
        }
        if (accountNumber == null || !accountNumber.matches("\\d{9,18}")) {
            return "Invalid account number. Must be 9-18 digits.";
        }
        // IFSC: 4 letters + 0 + 6 alphanumeric  (e.g. SBIN0001234)
        if (ifscCode == null || !ifscCode.matches("[A-Z]{4}0[A-Z0-9]{6}")) {
            return "Invalid IFSC code. Expected format: SBIN0001234";
        }
        return null;
    }

    @Override
    public boolean process() {
        String masked = accountNumber.substring(0, 4) + "****" + accountNumber.substring(accountNumber.length() - 2);
        System.out.println("  [NET BANKING] Initiating NEFT from account " + masked + " at " + bankName + " (IFSC: " + ifscCode + ") ...");
        simulateGatewayDelay();
        System.out.println("  [NET BANKING] Bank confirmed the transfer.");
        setStatus("SUCCESS");
        setRemarks("Net banking via " + bankName + " — IFSC " + ifscCode);
        return true;
    }

    private void simulateGatewayDelay() {
        try { Thread.sleep(1400); } catch (InterruptedException ignored) {}
    }
}
