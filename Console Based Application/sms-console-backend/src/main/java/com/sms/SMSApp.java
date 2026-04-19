package com.sms;

import com.sms.controller.LoginController;
import com.sms.util.AppScanner;

//Author: Vigneshwaran M 
/* This class is for the main method of the application
 * Here we have login prompt for user
 */

public class SMSApp {

    public static void main(String[] args) {
   
    	//Object for login controller
        LoginController loginController = new LoginController();

        //This loop is for the user choice
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
               
                //Calling the login method
                case 1: loginController.login(); break;
                
                //Calling the forgot password method
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