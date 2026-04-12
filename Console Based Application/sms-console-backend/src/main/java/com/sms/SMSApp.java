package com.sms;

import java.util.Scanner;

import com.sms.controller.LoginController;


public class SMSApp{

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        
        LoginController loginController = new LoginController();
      
        while(true) {

            System.out.println("----------------Online School Management System----------------");
            System.out.print("1. Login with email: ");
            System.out.print("2. Forgot password ");
            System.out.print("3. Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:
                	loginController.login();
                    break;

                case 2:
                    loginController.login();
                    break;

                case 3:
                    System.out.println("Exiting online school management system!");
                    System.out.println("Thank you!");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("\nInvalid choice. Please enter 1, 2 or 3.");
            }
          }
        }
}