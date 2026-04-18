package com.sms.util;

// Author: Vigneshwaran M
/*
 * // This class is used to validate user input like email, phone, password, etc.
 */
import java.util.regex.Pattern;

public class InputValidator {
	
    // Regex pattern for validating email format
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // Regex pattern for validating phone number (10 digits, starts with 6-9)
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[6-9][0-9]{9}$");

    // Regex pattern for validating password (8-20 chars, uppercase, lowercase, number, special char)
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$");

    // Validate email
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    // Validate phone number
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    // Validate password
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return PASSWORD_PATTERN.matcher(password.trim()).matches();
    }

    // Check if input is not empty
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}