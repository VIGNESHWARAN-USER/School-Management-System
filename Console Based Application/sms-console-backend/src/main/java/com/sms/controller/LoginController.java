package com.sms.controller;

import java.util.Scanner;

import com.sms.entities.User;
import com.sms.exception.UserNotFoundException;

public class LoginController {

    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();

    public void login() {
        System.out.print("Enter your email    : ");
        String email = sc.next();

        System.out.print("Enter your password : ");
        String password = sc.next();

        try {
            User user = userService.login(email, password);

            if(user != null) {
                System.out.println(ColorText.success("\n  Login Successful! Welcome, " + user.getName()));

                if(user instanceof Admin) {
                    System.out.println(ColorText.yellow("  Redirecting to Admin Dashboard..."));
                    new AdminController().adminMenu();
                } else if(user instanceof Customer) {
                    System.out.println(ColorText.yellow("  Redirecting to Customer Dashboard..."));
                    new CustomerController(user.getUserId()).customerMenu();
                }
            } else {
                System.out.println(ColorText.error("\n  Invalid email or password. Please try again."));
            }
        } catch (InvalidCredentialsException e) {
            System.out.println(ColorText.error("\n  " + e.getMessage()));
        }
    }

    public void forgotPassword() {

        System.out.println(ColorText.warning("\n┌─────────────────────────────────────┐"));
        System.out.println(ColorText.warning("│") + ColorText.bold("           FORGOT PASSWORD           ") + ColorText.warning("│"));
        System.out.println(ColorText.warning("└─────────────────────────────────────┘"));

        System.out.print("  Registered Email: ");
        String email = sc.next();

        try {
            User user = userService.getUserByEmail(email);

            if(user == null) {
                System.out.println(ColorText.error("\n  Email not registered. Please check and try again."));
                return;
            }

            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
            long otpGeneratedTime = System.currentTimeMillis();

            System.out.println(ColorText.warning("\n  Sending OTP to your email..."));
            EmailUtil.sendOTPEmail(email, user.getName(), otp);
            System.out.println(ColorText.success("  OTP sent successfully!"));
            System.out.println(ColorText.warning("  Note: OTP is valid for 5 minutes only."));

            System.out.print(ColorText.bold("\n  Enter OTP: "));
            String enteredOtp = sc.next();

            if(System.currentTimeMillis() - otpGeneratedTime > 5 * 60 * 1000) {
                System.out.println(ColorText.error("\n  OTP expired. Please try again."));
                return;
            }

            if(enteredOtp.equals(otp)) {
                System.out.print("\n  Enter new password : ");
                String newPassword = sc.next();

                System.out.print("  Confirm password   : ");
                String confirmPassword = sc.next();

                if(!newPassword.equals(confirmPassword)) {
                    System.out.println(ColorText.error("\n  Passwords do not match. Please try again."));
                    return;
                }

                userService.updatePassword(email, newPassword);
                System.out.println(ColorText.success("\n  Password reset successful! Please login again."));
            } else {
                System.out.println(ColorText.error("\n  Invalid OTP. Please try again."));
            }
        } catch (UserNotFoundException e) {
            System.out.println("\n  " + e.getMessage());
        }
    }
}