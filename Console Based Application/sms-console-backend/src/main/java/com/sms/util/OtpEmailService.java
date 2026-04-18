package com.sms.util;

import java.util.Random;

public class OtpEmailService {

    /** Generate a random 4-digit OTP string (1000-9999) */
    public static String generateOtp() {
        int otp = 1000 + new Random().nextInt(9000);
        return String.valueOf(otp);
    }

    /**
     * Send OTP to the given email using the existing EmailUtil infrastructure.
     * Uses the otp_email.html template in resources.
     * @return true if sent without exception, false otherwise.
     */
    public static boolean sendOtp(String recipientEmail, String otp) {
        try {
            String userName = recipientEmail.contains("@")
                ? recipientEmail.substring(0, recipientEmail.indexOf('@'))
                : recipientEmail;

            return EmailUtil.sendOTPEmail(recipientEmail, userName, otp);
        } catch (Exception e) {
            System.out.println("  Failed to send OTP email: " + e.getMessage());
            return false;
        }
    }
}
