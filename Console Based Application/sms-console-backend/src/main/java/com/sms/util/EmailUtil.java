package com.sms.util;

// Author: Vigneshwaran M

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

// This class is used to handle all email related operations in the application
public class EmailUtil {

    // Sender email credentials
    private static final String FROM_EMAIL = "2k22cse163@kiot.ac.in";
    private static final String PASSWORD   = "uygu rsyv xvtx uvod";

    // Creating mail session with SMTP configuration
    private static Session createSession() {

        Properties props = new Properties();

        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");
        props.put("mail.smtp.ssl.trust","smtp.gmail.com");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.writetimeout", "5000");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
    }

    // Load HTML template from resources and replace placeholders
    private static String loadTemplate(String fileName, String userName) {
        try {
            InputStream is = EmailUtil.class
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            if (is == null) {
                System.out.println("Template file not found: " + fileName);
                return null;
            }

            String html = new BufferedReader( new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            html = html.replace("{{userName}}", userName);

            return html;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    

    // Send OTP email for password reset
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

        } catch (Exception e) {
            System.out.println("  OTP EMAIL FAILED: " + e.getMessage());
            return false;
        }
    }

}