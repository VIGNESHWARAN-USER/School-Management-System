package com.sms.controller;

import com.sms.entities.User;
import com.sms.util.AppScanner;

public class StudentController {

    private final java.util.Scanner sc = AppScanner.get();
    
    public void studentMenu(User user) {

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
            try { choice = Integer.parseInt(sc.nextLine()); }
            catch(Exception e) { System.out.println("Invalid."); continue; }

            switch(choice) {
                case 1: new MembersController().getUser(user.getId()); break;
                case 2: new FeeStructureController().getFeeStructure(user.getId(), false); break;
                case 3: new PaymentController().getPayment(user.getId()); break;
                case 4: new EventController().studentEventsMenu(user.getId()); break;
                case 5: new ExamController().studentResultsMenu(user.getId()); break;
                case 6:
                    System.out.println("  Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}