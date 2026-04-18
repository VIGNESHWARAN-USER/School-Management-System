package com.sms.entities;

/**
 * Credit Card Payment — validates 16-digit card number, expiry (MM/YY), CVV (3 digits).
 */
public class CreditCardPayment extends Payment {

    private String cardNumber;
    private String cardHolderName;
    private String expiry;   // MM/YY
    private String cvv;

    public CreditCardPayment(double amountPaid, String cardNumber, String cardHolderName,
                             String expiry, String cvv) {
        super(amountPaid, "CREDIT_CARD");
        this.cardNumber = cardNumber.replaceAll("\\s+", ""); // strip spaces
        this.cardHolderName = cardHolderName;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    @Override
    public String validate() {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            return "Invalid card number. Must be exactly 16 digits.";
        }
        if (cardHolderName == null || cardHolderName.trim().length() < 3) {
            return "Card holder name must be at least 3 characters.";
        }
        if (expiry == null || !expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            return "Invalid expiry date. Expected format: MM/YY (e.g. 08/27)";
        }
        if (cvv == null || !cvv.matches("\\d{3}")) {
            return "Invalid CVV. Must be 3 digits.";
        }
        // Check expiry is not in the past
        String[] parts = expiry.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]) + 2000;
        java.time.YearMonth cardExpiry = java.time.YearMonth.of(year, month);
        if (cardExpiry.isBefore(java.time.YearMonth.now())) {
            return "The card has expired.";
        }
        return null;
    }

    @Override
    public boolean process() {
        // Mask card number for display
        String masked = "**** **** **** " + cardNumber.substring(12);
        System.out.println("  [CREDIT CARD] Charging card " + masked + " held by " + cardHolderName + " ...");
        simulateGatewayDelay();
        System.out.println("  [CREDIT CARD] Transaction approved by bank.");
        setStatus("SUCCESS");
        setRemarks("Credit card payment — card ending " + cardNumber.substring(12));
        return true;
    }

    private void simulateGatewayDelay() {
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
    }
}
