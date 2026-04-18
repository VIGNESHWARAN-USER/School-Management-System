package com.sms.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

    private static final String FROM_EMAIL = "2k22cse163@kiot.ac.in";
    private static final String PASSWORD   = "uygu rsyv xvtx uvod";

    private static Session createSession() {

        Properties props = new Properties();

        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");
        props.put("mail.smtp.ssl.trust","smtp.gmail.com");
        props.put("mail.smtp.connectiontimeout", "5000");  // 5s connect timeout
        props.put("mail.smtp.timeout", "5000");            // 5s read timeout
        props.put("mail.smtp.writetimeout", "5000");       // 5s write timeout

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
    }

    private static String loadTemplate(String fileName, String userName) {
        try {
            InputStream is = EmailUtil.class
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            if (is == null) {
                System.out.println("Template file not found: " + fileName);
                return null;
            }

            String html = new BufferedReader(
                    new InputStreamReader(is,StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            html = html.replace("{{userName}}", userName);

            return html;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendWelcomeEmail(String toEmail, String userName) {
        try {
        	
            String html = loadTemplate("welcome_email.html", userName);
            if (html == null) return;

            MimeMessage message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Welcome to Tour & Travel System!");
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);

        }
        catch (Exception e) {
            System.out.println("EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendCancellationEmail(String toEmail, String userName, int bookingId) {
        try {
            String html = loadTemplate("cancellation_email.html", userName);
            if (html == null) return;

            html = html.replace("{{bookingId}}", String.valueOf(bookingId));

            MimeMessage message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Booking Cancellation - ID #" + bookingId);
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);

        }
        catch (Exception e) {
            System.out.println("CANCELLATION EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendBookingConfirmationEmail(String toEmail, String userName,
            int bookingId, int packageId,
            int travelers, double totalAmount,
            String bookingDate) {
        try {
            String html = loadTemplate("booking_confirmation.html", userName);
            if (html == null) return;

            html = html.replace("{{bookingId}}",   String.valueOf(bookingId));
            html = html.replace("{{packageId}}",   String.valueOf(packageId));
            html = html.replace("{{travelers}}",   String.valueOf(travelers));
            html = html.replace("{{totalAmount}}", String.valueOf(totalAmount));
            html = html.replace("{{bookingDate}}", bookingDate);

            MimeMessage message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Booking Confirmed - ID #" + bookingId);
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);

        }
        catch (Exception e) {
            System.out.println("BOOKING EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean sendOTPEmail(String toEmail, String userName, String otp) {
        try {
        	
            String html = loadTemplate("otp_email.html", userName);
            if (html == null) {
                System.out.println("  OTP email template not found.");
                return false;
            }

            html = html.replace("{{otp}}", otp);

            MimeMessage message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your OTP for Password Reset");
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);
            return true;

        }
        catch (Exception e) {
            System.out.println("  OTP EMAIL FAILED: " + e.getMessage());
            return false;
        }
    }
    
    public static void sendOTPEmailForAdminCredential(String toEmail, String requesterName, String requesterEmail, String otp) {
        try {
            // Using empty string for userName since template has {{requesterName}}
            String html = loadTemplate("admin_credential_otp.html", "");
            if (html == null) return;

            html = html.replace("{{otp}}", otp);
            html = html.replace("{{requesterName}}", requesterName);
            html = html.replace("{{requesterEmail}}", requesterEmail);

            MimeMessage message = new MimeMessage(createSession());
            
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Admin Credential Request OTP");
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("OTP email sent to " + toEmail);

        }
        catch (Exception e) {
            System.out.println("OTP EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendAdminBookingAlertEmail(String adminEmail,
            String customerName, int customerId,
            int bookingId, int packageId,
            int travelers, double totalAmount,
            String bookingDate) {
    	
        try {
            String html = loadTemplate("admin_booking_alert.html", "Admin");
            if (html == null) return;

            html = html.replace("{{customerName}}", customerName);
            html = html.replace("{{customerId}}",   String.valueOf(customerId));
            html = html.replace("{{bookingId}}",    String.valueOf(bookingId));
            html = html.replace("{{packageId}}",    String.valueOf(packageId));
            html = html.replace("{{travelers}}",    String.valueOf(travelers));
            html = html.replace("{{totalAmount}}",  String.valueOf(totalAmount));
            html = html.replace("{{bookingDate}}",  bookingDate);

            MimeMessage message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail));
            message.setSubject("New Booking Alert - ID #" + bookingId);
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Admin booking alert sent.");

        }
        catch (Exception e) {
            System.out.println("ADMIN ALERT EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendAdminCancellationAlertEmail(String adminEmail,
            String customerName, int customerId, int bookingId) {
        try {
            String html = loadTemplate("admin_cancellation_alert.html", "Admin");
            if (html == null) return;

            html = html.replace("{{customerName}}", customerName);
            html = html.replace("{{customerId}}",   String.valueOf(customerId));
            html = html.replace("{{bookingId}}",    String.valueOf(bookingId));

            MimeMessage message = new MimeMessage(createSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail));
            message.setSubject("Booking Cancelled - ID #" + bookingId);
            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Admin cancellation alert sent.");

        }
        catch (Exception e) {
            System.out.println("ADMIN CANCEL EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}