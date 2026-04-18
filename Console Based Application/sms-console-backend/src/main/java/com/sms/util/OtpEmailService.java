package com.sms.util;

// Author: Vigneshwaran M
/*
 * // This class is used to generate and send OTP via email
 */

import java.util.Random;

public class OtpEmailService {

    // Generate a random 4-digit OTP (range: 1000 - 9999)
    public static String generateOtp() {
        int otp = 1000 + new Random().nextInt(9000);
        return String.valueOf(otp);
    }

    // Send OTP to the given email using EmailUtil
    public static boolean sendOtp(String recipientEmail, String otp) {
        try {

            // Extract username from email (before '@')
            String userName = recipientEmail.contains("@")
                ? recipientEmail.substring(0, recipientEmail.indexOf('@'))
                : recipientEmail;

            // Call EmailUtil to send OTP email
            return EmailUtil.sendOTPEmail(recipientEmail, userName, otp);

        } catch (Exception e) {
            System.out.println("  Failed to send OTP email: " + e.getMessage());
            return false;
        }
    }
}