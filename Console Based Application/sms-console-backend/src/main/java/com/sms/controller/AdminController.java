package com.sms.controller;

import com.sms.util.AppScanner;
import com.sms.util.InputReader;
// Author : Reshma K
/*
 * This Controller is to prompt the admin menu
 * This menu includes members, resources, fees structures, events and exams
 */
public class AdminController {
	
	// Getting scanner object
    private final InputReader sc = InputReader.get();
    
    // Creating objects for controllers
    MembersController membersController = new MembersController();
    ResourceController resourceController = new ResourceController();
    FeeStructureController feeStructureController = new FeeStructureController();
    EventController eventController = new EventController();
    ExamController examController = new ExamController();
    
    //This is the main menu for admin
    public void adminMenu() {

        while (true) {
            System.out.println("  ADMIN DASHBOARD");
            System.out.println("  1. Manage Members");
            System.out.println("  2. Manage Resources");
            System.out.println("  3. Manage Fee Structures");
            System.out.println("  4. Manage Events");
            System.out.println("  5. Manage Exams");
            System.out.println("  6. Log Out");

            System.out.print("  Enter choice: ");
            int choice = Integer.parseInt(sc.readLine());
            System.out.println(choice);
            //Calling other menu's from main menu
            switch(choice) {
                case 1: membersMenu(); break;
                case 2: resourcesMenu(); break;
                case 3: feeStructureMenu(); break;
                case 4: eventsMenu(); break;
                case 5: examController.adminExamMenu(); break;
                case 6:
                    System.out.println("  Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

   // This is the members menu
    private void membersMenu() {

        while(true) {

            System.out.println("MEMBERS MENU");
            System.out.println("1. Add Member");
            System.out.println("2. Edit Member");
            System.out.println("3. Delete Member");
            System.out.println("4. Show Members");
            System.out.println("5. Exit");
            

            System.out.print("Enter your choice : ");
            int c = Integer.parseInt(sc.readLine());
            System.out.println(c);
            // Calling the corresponding controller method
            switch(c) {
                case 1: membersController.addMember(); break;
                case 2: membersController.editMember(); break;
                case 3: membersController.deleteMember(); break;
                case 4: membersController.showMembers(); break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // This is the resources menu
    private void resourcesMenu(){
        while(true) {

            System.out.println("RESOURCES MENU");
            System.out.println("1. Add ClassRoom");
            System.out.println("2. Delete ClassRoom");
            System.out.println("3. Show ClassRooms");
            System.out.println("4. Add Subject");
            System.out.println("5. Delete Subject");
            System.out.println("6. Show Subjects");
            System.out.println("7. Exit");
            

            System.out.print("Enter your choice : ");
            int c = Integer.parseInt(sc.readLine());
            System.out.println(c);
         // Calling the corresponding controller method
            switch(c) {
                case 1: resourceController.addClassRoom(); break;
                case 2: resourceController.deleteClassRoom(); break;
                case 3: resourceController.showClassRooms(); break;
                case 4: resourceController.addSubject(); break;
                case 5: resourceController.deleteSubject(); break;
                case 6: resourceController.showSubjects(); break;
                case 7: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // This is the feeStructure menu
    private void feeStructureMenu() {
        while(true) {

            System.out.println("FEE STRUCTURE MENU");
            System.out.println("1. Add Fee Structure");
            System.out.println("2. Edit Fee Structure");
            System.out.println("3. Delete Fee Structure");
            System.out.println("4. Show Fee Structures");
            System.out.println("5. Exit");
            

            System.out.print("Enter your choice : ");
            int c = Integer.parseInt(sc.readLine());
           System.out.println(c);
         // Calling the corresponding controller method
            switch(c) {
                case 1: feeStructureController.addFeeStructure(); break;
                case 2: feeStructureController.editFeeStructure(); break;
                case 3: feeStructureController.deleteFeeStructure(); break;
                case 4: feeStructureController.showFeeStructures(); break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // This is the events menu
    private void eventsMenu() {
        while(true) {
            System.out.println("EVENTS MENU");
            System.out.println("1. Add Event");
            System.out.println("2. Edit Event");
            System.out.println("3. Delete Event");
            System.out.println("4. View All Events");
            System.out.println("5. View Event Participants");
            System.out.println("6. Exit");
            System.out.print("Enter your choice : ");
            int c = Integer.parseInt(sc.readLine());
            System.out.println(c);
         // Calling the corresponding controller method
            switch(c) {
                case 1: eventController.addEvent(); break;
                case 2: eventController.editEvent(); break;
                case 3: eventController.deleteEvent(); break;
                case 4: eventController.showAllEvents(); break;
                case 5: eventController.showParticipants(); break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
}