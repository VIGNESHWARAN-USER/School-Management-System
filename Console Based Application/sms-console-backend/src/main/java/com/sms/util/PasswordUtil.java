package com.sms.util;

//Author:Vigneshwaran M
/*
 * This class is for hashing password and comparing with the raw password
 */
import java.security.MessageDigest;

public class PasswordUtil {
//This method is for hashing password using SHA-256 algorithm
    public static String hashPassword(String password) {

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();

            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//Comparing raw password and hashed password 
    public static boolean verifyPassword(String inputPassword, String storedPassword) {

        String hashedInput = hashPassword(inputPassword);

        return hashedInput.equals(storedPassword);
    }
}