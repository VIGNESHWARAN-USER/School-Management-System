package com.sms.controller;

import java.util.Scanner;

public class AdminController {

    Scanner sc = new Scanner(System.in);
    
    MembersController membersController = new MembersController();
    ResourceController resourceController = new ResourceController();
    
    public void adminMenu() {

        while (true) {
            System.out.println("  ADMIN DASHBOARD");
            System.out.println("  1. Manage Members");
            System.out.println("  2. Manage Resources");
            System.out.println("  3. Log Out");
           

            System.out.print("  Enter choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch(choice) {
                
                case 1: membersMenu(); break;
                case 2: resourcesMenu(); break;
                case 3:
                    System.out.println("  Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

   
    private void membersMenu() {

        while(true) {

            System.out.println("MEMBERS MENU");
            System.out.println("1. Add Member");
            System.out.println("2. Edit Member");
            System.out.println("3. Delete Member");
            System.out.println("4. Show Members");
            System.out.println("5. Exit");
            

            System.out.print("Enter your choice : ");
            int c = Integer.parseInt(sc.nextLine());

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
            int c = Integer.parseInt(sc.nextLine());

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
}