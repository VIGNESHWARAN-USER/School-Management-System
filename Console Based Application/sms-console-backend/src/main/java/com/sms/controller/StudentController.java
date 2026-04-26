package com.sms.controller;

// Vigneshwaran M
/*
 * This Controller handles all student related functionalities
 * It includes viewing profile, fees, payments, events and exam results
 */

import com.sms.entities.User;
import com.sms.util.AppScanner;
import com.sms.util.InputReader;

public class StudentController {

    // Getting scanner object
    private final InputReader sc = InputReader.get();
    
    // Student dashboard menu
    public void studentMenu(User user) {

        // Loop to keep menu running
        while (true) {
            System.out.println("  STUDENT DASHBOARD");
            System.out.println("  1. View Profile");
            System.out.println("  2. View Fee Structure");
            System.out.println("  3. View Payments");
            System.out.println("  4. Events");
            System.out.println("  5. View Exam Results");
            System.out.println("  6. Log Out");

            System.out.print("  Enter choice: ");
            
            int choice;
            try {
                // Conversion method
                choice = Integer.parseInt(sc.readLine());
                System.out.println(choice);
            }
            catch(Exception e) {
                System.out.println("Invalid.");
                continue;
            }

            // Switch case to handle different options
            switch(choice) {
                
                case 1:
                    // View student profile
                    new MembersController().getUser(user.getId());
                    break;
                
                case 2:
                    // View fee structure without payment option
                    new FeeStructureController().getFeeStructure(user.getId(), false);
                    break;
                
                case 3:
                    // View payment history
                    new PaymentController().getPayment(user.getId());
                    break;
                
                case 4:
                    // View and register for events
                    new EventController().studentEventsMenu(user.getId());
                    break;
                
                case 5:
                    // View exam results
                    new ExamController().studentResultsMenu(user.getId());
                    break;
                
                case 6:
                    // Logout option
                    System.out.println("  Logging out...");
                    return;
                
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}