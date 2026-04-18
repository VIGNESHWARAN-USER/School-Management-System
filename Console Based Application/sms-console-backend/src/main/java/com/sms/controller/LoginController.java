package com.sms.controller;
//Author: Vigneshwaran M 
/*
 * This controller is for the user login
 * Here we have the login based on the roles like admin,teacher,student,parent
 * Also includes forgot password method 
 */
import com.sms.entities.User;
import com.sms.exception.InvalidCredentialsException;
import com.sms.service.UserService;
import com.sms.util.AppScanner;

public class LoginController {
    //Declaring the Scanner
    private final java.util.Scanner sc = AppScanner.get();
    //Creating the service object
    UserService userService = new UserService();
    
    //We are collecting the user email and password by prompting and pass to the service method
    public void login() {
        System.out.print("Enter your email    : ");
        String email = sc.nextLine().trim();//String handling

        System.out.print("Enter your password : ");
        String password = sc.nextLine().trim();//String handling

        try {
            User user = userService.login(email, password);
            if(user != null) {
                System.out.println("Login Successful! Welcome, " + user.getName());

                //Calling corresponding controller according to the role
                if(user.getRole().equalsIgnoreCase("ADMIN")) {
                    System.out.println("Redirecting to Admin Dashboard...");
                    new AdminController().adminMenu();
                } else if(user.getRole().equalsIgnoreCase("STUDENT")) {
                    System.out.println("  Redirecting to Student Dashboard...");
                    new StudentController().studentMenu(user);
                } else if(user.getRole().equalsIgnoreCase("PARENT")) {
                    System.out.println("  Redirecting to Parent Dashboard...");
                    new ParentController().parentMenu(user);
                } else if(user.getRole().equalsIgnoreCase("TEACHER")) {
                    System.out.println("  Redirecting to Teacher Dashboard...");
                    new TeacherController().teacherMenu(user);
                }
            } else {
                System.out.println("\n  Invalid email or password. Please try again.");
            }
            
        } catch (InvalidCredentialsException e)//Using domain based exception
        {
            System.out.println("\n  " + e.getMessage());
        }
    }

    /*This method is used to get email from the user and then creating the four digit OTP
     *and sending email to the user
     */
    
    public void forgotPassword() {
        System.out.print("\nEnter your registered email: ");
        String email = sc.nextLine().trim();
  
        //Checking the email is present in the database or not
        if (!new com.sms.dao.UserDAO().isEmailExists(email)) {
            System.out.println("  Email not found in the system.");
            return;
        }

        // Generate and send OTP
        String otp = com.sms.util.OtpEmailService.generateOtp();
        System.out.println("  Sending OTP to " + email + " ...");
        boolean sent = com.sms.util.OtpEmailService.sendOtp(email, otp);
        if (!sent) {
            System.out.println("  Could not send OTP email. Please try again later.");
            return;
        }
        System.out.println("  OTP sent! Check your inbox.");

        // OTP entry  allow up to 3 attempts
        boolean verified = false;
        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.print("  Enter OTP (attempt " + attempt + "/3): ");
            String entered = sc.nextLine().trim();
            if (otp.equals(entered)) {
                verified = true;
                break;
            }
            System.out.println("  Incorrect OTP." + (attempt < 3 ? " Try again." : ""));
        }

        if (!verified) {
            System.out.println("  Too many failed attempts. Password reset cancelled.");
            return;
        }

        // Reset password
        System.out.print("  Enter new password (min 4 characters): ");
        String newPassword = sc.nextLine().trim();
        if (newPassword.length() < 4) {
            System.out.println("  Weak password. Minimum 4 characters required.");
            return;
        }

        //Confirm new password
        System.out.print("  Confirm new password: ");
        String confirm = sc.nextLine().trim();
        if (!newPassword.equals(confirm)) {
            System.out.println("  Passwords do not match. Please try again.");
            return;
        }
        
        //Calling the update password to update in database
        userService.updatePassword(email, newPassword);
        System.out.println("  Password reset successfully! You can now log in.");
    }
}