package com.sms;

import com.sms.controller.LoginController;
import com.sms.util.AppScanner;

public class SMSApp {

    public static void main(String[] args) {

        LoginController loginController = new LoginController();

        while(true) {
            System.out.println("----------------Online School Management System----------------");
            System.out.println("1. Login with email");
            System.out.println("2. Forgot password ");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(AppScanner.get().nextLine().trim());
            } catch(Exception e) {
                System.out.println("\nInvalid choice. Please enter 1, 2 or 3.");
                continue;
            }

            switch(choice) {
                case 1: loginController.login(); break;
                case 2: loginController.forgotPassword(); break;
                case 3:
                    System.out.println("Exiting online school management system!");
                    System.out.println("Thank you!");
                    System.exit(0);
                default:
                    System.out.println("\nInvalid choice. Please enter 1, 2 or 3.");
            }
        }
    }
}