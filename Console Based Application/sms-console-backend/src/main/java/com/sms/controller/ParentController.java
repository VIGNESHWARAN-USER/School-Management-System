package com.sms.controller;

// Vigneshwaran M
/*
 * This Controller handles all Parent related functionalities
 * It includes managing children, viewing fees, events and transactions
 */

import com.sms.entities.Parent;
import com.sms.entities.User;
import com.sms.util.AppScanner;

public class ParentController {

    // Parent dashboard menu
    public void parentMenu(User user) {
        
        // Type casting from User to Parent
        Parent parent = (Parent) user;
        
        // Getting scanner object
        java.util.Scanner sc = AppScanner.get();
        
        // Loop to keep dashboard running
        while (true) {
            System.out.println("\n  PARENT DASHBOARD");
            System.out.println("  1. View & Pay Fees for a Child");
            System.out.println("  2. View Events");
            System.out.println("  3. Log Out");
            System.out.print("  Enter choice: ");
            
            int choice;
            try {
                // Conversion method
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid choice!");
                continue;
            }

            if (choice == 1) {
                
                // Collection used to store child IDs
                if (parent.getChildIds() == null || parent.getChildIds().isEmpty()) {
                    System.out.println("You have no children linked to your account.");
                    continue;
                }
                
                System.out.println("Your Children IDs: " + parent.getChildIds());
                System.out.print("Enter Student ID to manage: ");
                
                try {
                    // Conversion method
                    long studentId = Long.parseLong(sc.nextLine().trim());
                    
                    // Checking if entered ID exists in collection
                    if (!parent.getChildIds().contains(studentId)) {
                        System.out.println("This student ID is not linked to your account!");
                    } else {
                        manageChildMenu(studentId);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid numeric input!");
                }

            } else if (choice == 2) {
                
                // String handling and conditional operator
                // Getting first child ID from collection if available
                Long firstChild = (parent.getChildIds() != null && !parent.getChildIds().isEmpty())
                    ? parent.getChildIds().get(0) : null;
                
                new EventController().viewerEventsMenu("PARENT", firstChild);

            } else if (choice == 3) {
                
                System.out.println("Logging out...");
                break;

            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    // Menu to manage specific child
    private void manageChildMenu(long studentId) {
        
        // Getting scanner object
        java.util.Scanner sc = AppScanner.get();
        
        // Loop for child menu
        while(true) {
            System.out.println("\n  --- Manage Child (ID: " + studentId + ") ---");
            System.out.println("  1. View Profile");
            System.out.println("  2. View & Pay Fees");
            System.out.println("  3. View Transaction History");
            System.out.println("  4. View Child's Events");
            System.out.println("  5. View Exam Results");
            System.out.println("  6. Back");
            System.out.print("  Enter choice: ");
            
            int c;
            try {
                // Conversion method
                c = Integer.parseInt(sc.nextLine().trim());
            } catch(Exception e) {
                System.out.println("Invalid choice!");
                continue;
            }
            
            if (c == 1) {
                
                new MembersController().getUser(studentId);

            } else if (c == 2) {
                
                new FeeStructureController().getFeeStructure(studentId, true);

            } else if (c == 3) {
                
                new PaymentController().getPayment(studentId);

            } else if (c == 4) {
                
                new EventController().viewerEventsMenu("PARENT", studentId);

            } else if (c == 5) {
                
                new ExamController().studentResultsMenu(studentId);

            } else if (c == 6) {
                
                break;

            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}