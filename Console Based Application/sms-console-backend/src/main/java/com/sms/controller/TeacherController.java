package com.sms.controller;

import com.sms.entities.Teacher;
import com.sms.entities.User;
import com.sms.util.AppScanner;

public class TeacherController {

    private final java.util.Scanner sc = AppScanner.get();

    public void teacherMenu(User user) {
        Teacher teacher = (Teacher) user;
        while (true) {
            System.out.println("\n  TEACHER DASHBOARD - " + teacher.getName());
            System.out.println("  1. View All Members");
            System.out.println("  2. View Events & Participants");
            System.out.println("  3. Manage Grades / Results");
            System.out.println("  4. Log Out");
            System.out.print("  Enter choice: ");
            int choice;
            try { choice = Integer.parseInt(sc.nextLine().trim()); }
            catch(Exception e) { System.out.println("Invalid."); continue; }

            if (choice == 1) {
                new MembersController().showMembers();
            } else if (choice == 2) {
                new EventController().viewerEventsMenu("TEACHER", null);
            } else if (choice == 3) {
                new ExamController().teacherGradingMenu(teacher.getClassId());
            } else if (choice == 4) {
                System.out.println("  Logging out...");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}

