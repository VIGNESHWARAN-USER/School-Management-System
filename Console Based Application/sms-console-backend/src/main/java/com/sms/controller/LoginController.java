package com.sms.controller;

import java.util.Scanner;

import com.sms.entities.User;
import com.sms.exception.InvalidCredentialsException;
import com.sms.exception.UserNotFoundException;
import com.sms.service.UserService;

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
                System.out.println("Login Successful! Welcome, " + user.getName());

                if(user.getRole().equalsIgnoreCase("ADMIN")) {
                    System.out.println("Redirecting to Admin Dashboard...");
                    new AdminController().adminMenu();
                } else if(user.getRole().equalsIgnoreCase("STUDENT")) {
                    System.out.println("  Redirecting to Student Dashboard...");
                    new StudentController().studentMenu(user);
                }
                else if(user.getRole().equalsIgnoreCase("PARENT")) {
                    System.out.println("  Redirecting to Parent Dashboard...");
                    new ParentController().parentMenu(user);
                }
                else if(user.getRole().equalsIgnoreCase("TEACHER")) {
                    System.out.println("  Redirecting to Teacher Dashboard...");
                    new TeacherController().teacherMenu();
                }
            } else {
                System.out.println("\n  Invalid email or password. Please try again.");
            }
        } catch (InvalidCredentialsException e) {
            System.out.println("\n  " + e.getMessage());
        }
    }

}