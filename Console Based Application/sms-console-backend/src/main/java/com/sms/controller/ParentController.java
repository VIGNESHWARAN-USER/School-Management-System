package com.sms.controller;

import com.sms.entities.Parent;
import com.sms.entities.User;
import java.util.Scanner;

public class ParentController {

    public void parentMenu(User user) {
        Parent parent = (Parent) user;
        Scanner sc = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n  PARENT DASHBOARD");
            System.out.println("  1. View & Pay Fees for a Child");
            System.out.println("  2. Log Out");
            System.out.print("  Enter choice: ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid choice!");
                continue;
            }

            if (choice == 1) {
                if (parent.getChildIds() == null || parent.getChildIds().isEmpty()) {
                    System.out.println("You have no children linked to your account.");
                    continue;
                }
                
                System.out.println("Your Children IDs: " + parent.getChildIds());
                System.out.print("Enter Student ID to manage: ");
                try {
                    long studentId = Long.parseLong(sc.nextLine());
                    if (!parent.getChildIds().contains(studentId)) {
                        System.out.println("This student ID is not linked to your account!");
                    } else {
                        new FeeStructureController().getFeeStructure(studentId);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid numeric input!");
                }
            } else if (choice == 2) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
