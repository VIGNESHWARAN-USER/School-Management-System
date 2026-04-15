package com.sms.controller;

import java.util.Scanner;
import com.sms.entities.User;

public class StudentController {

    Scanner sc = new Scanner(System.in);
    
    public void studentMenu(User user) {

        while (true) {
            System.out.println("  STUDENT DASHBOARD");
            System.out.println("  1. View Profile");
            System.out.println("  2. View Fee Structure");
            System.out.println("  3. View Payements");
            System.out.println("  4. Log Out");
           

            System.out.print("  Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {
                
                case 1: new MembersController().getUser(user.getId()); break;
                case 2: new FeeStructureController().getFeeStructure(user.getId()); break;
                //case 3: new PaymentController().getPayment(user.getId()); break;
                case 4:
                    System.out.println("  Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}