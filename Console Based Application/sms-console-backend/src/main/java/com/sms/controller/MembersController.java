package com.sms.controller;

// Author : Vigneshwaran M
/*
 * This Controller handles all Member related functionalities
 * It includes adding, updating, deleting and viewing members
 */

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.sms.entities.Parent;
import com.sms.entities.Student;
import com.sms.entities.Teacher;
import com.sms.entities.User;
import com.sms.service.MembersService;
import com.sms.util.AppScanner;
import com.sms.util.InputValidator;

public class MembersController {
	
    // Getting scanner object
    private final java.util.Scanner sc = AppScanner.get();
    
    // Creating Service object
    private final MembersService membersService = new MembersService();
    
    // Default password for new users
    private final String DEFAULT_PASSWORD = "1234";

    // Add new member
	public void addMember()
	{
		System.out.println("\n--- Add New Member ---");
        System.out.println("Select Role:");
        System.out.println("1. Student");
        System.out.println("2. Teacher");
        System.out.println("3. Parent");
        System.out.print("Enter choice: ");
        int choice;
        try {
            // Conversion method
            choice = Integer.parseInt(sc.nextLine());
        } catch(NumberFormatException e) {
            System.out.println("Invalid input!");
            return;
        }
        
        if(choice < 1 || choice > 3) {
            System.out.println("Invalid choice!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine(); // String handling
        
        System.out.print("Enter Email: ");
        String email = sc.nextLine(); // String handling
        
        // Email validation
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        User newUser = null;

        try {
            if (choice == 1) { 
                // Student creation
                System.out.print("Enter Age: ");
                
                // Conversion method
                int age = Integer.parseInt(sc.nextLine());
                
                System.out.print("Enter Address: ");
                String address = sc.nextLine();
                
                System.out.print("Enter Parent's Email: ");
                String parentEmail = sc.nextLine();
                
                new ResourceController().showClassRooms();
                System.out.print("Enter Class ID: ");
                
                // Conversion method
                long classId = Long.parseLong(sc.nextLine());

                Student s = new Student(0L, name, email, DEFAULT_PASSWORD, "STUDENT", age, address, parentEmail, classId);
                newUser = s;

            } else if (choice == 2) { // Teacher
                
                System.out.print("Enter Phone Number: ");
                String phone = sc.nextLine();
                
                new ResourceController().showSubjects();
                System.out.print("Enter Subject ID: ");
                
                // Conversion method
                long subjectId = Long.parseLong(sc.nextLine());
                
                new ResourceController().showClassRooms();
                System.out.print("Enter Class ID: ");
                
                // Conversion method
                long classId = Long.parseLong(sc.nextLine());

                Teacher t = new Teacher(0L, name, email, DEFAULT_PASSWORD, "TEACHER", phone, subjectId, classId);
                newUser = t;

            } else if (choice == 3) { // Parent
                
                System.out.print("Enter Mobile Number: ");
                String mobile = sc.nextLine();
                
                System.out.print("Enter Address: ");
                String address = sc.nextLine();
                
                System.out.print("Enter Age: ");
                
                // Conversion method
                int age = Integer.parseInt(sc.nextLine());
                
                showStudents();
                
                System.out.print("Enter child Student IDs (comma separated): ");
                String idsStr = sc.nextLine();

                // Collection used to store multiple child IDs
                java.util.List<Long> childIds = new java.util.ArrayList<>();
                
                if (!idsStr.trim().isEmpty()) {
                    for (String idStr : idsStr.split(",")) {
                        childIds.add(Long.parseLong(idStr.trim())); // Conversion method
                    }
                }

                Parent p = new Parent(0L, name, email, DEFAULT_PASSWORD, "PARENT", mobile, address, age, childIds);
                newUser = p;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
            return;
        }

        if (newUser != null) {
            String result = membersService.addMember(newUser);
            System.out.println(result);
        }
	}
	
    // Delete member
	public void deleteMember()
	{
		showMembers();
        System.out.print("Enter User ID to delete: ");
        try {
            // Conversion method
            long userId = Long.parseLong(sc.nextLine());
            String res = membersService.deleteMember(userId);
            System.out.println(res);
        } catch(NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
	}
	
    // Edit member
	public void editMember()
	{
		showMembers();
        System.out.print("Enter User ID to edit: ");
        try {
            // Conversion method
            long userId = Long.parseLong(sc.nextLine());
            
            // Collection used
            List<User> members = membersService.getAllMembers();
            
            // Stream used to find user
            User toEdit = members.stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
            
            if (toEdit == null) {
                System.out.println("Member not found.");
                return;
            }

            System.out.println("Leave blank to keep existing value.");
            
            System.out.print("Enter New Name [" + toEdit.getName() + "]: ");
            String name = sc.nextLine();
            if (!name.trim().isEmpty()) toEdit.setName(name);

            System.out.print("Enter New Email [" + toEdit.getEmail() + "]: ");
            String email = sc.nextLine();
            if (!email.trim().isEmpty()) {
                if (InputValidator.isValidEmail(email)) {
                    toEdit.setEmail(email);
                } else {
                    System.out.println("Invalid email format! Keeping old email.");
                }
            }

            // Type checking using instanceof
            if (toEdit instanceof Student) {
                Student s = (Student) toEdit;

                System.out.print("Enter New Age [" + s.getAge() + "]: ");
                String age = sc.nextLine();
                if (!age.trim().isEmpty()) s.setAge(Integer.parseInt(age));

            } else if (toEdit instanceof Teacher) {
                Teacher t = (Teacher) toEdit;

                System.out.print("Enter New Phone Number [" + t.getPhoneNumber() + "]: ");
                String phone = sc.nextLine();
                if (!phone.trim().isEmpty()) t.setPhoneNumber(phone);

            } else if (toEdit instanceof Parent) {
                Parent p = (Parent) toEdit;

                System.out.print("Enter New Mobile Number [" + p.getMobileNumber() + "]: ");
                String mobile = sc.nextLine();
                if (!mobile.trim().isEmpty()) p.setMobileNumber(mobile);
            }

            String result = membersService.updateMember(toEdit);
            System.out.println(result);

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        }
	}
	
    // Show all members
	public void showMembers()
	{
        // Collection used
		List<User> members = membersService.getAllMembers();
        
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        // Stream filtering into separate collections
        List<User> students = members.stream().filter(u -> u instanceof Student).collect(Collectors.toList());
        List<User> teachers = members.stream().filter(u -> u instanceof Teacher).collect(Collectors.toList());
        List<User> parents = members.stream().filter(u -> u instanceof Parent).collect(Collectors.toList());

        System.out.println("\n--- Members List ---");
        
        // Formatting output
        if (!students.isEmpty()) {
            System.out.println("\n[ STUDENTS ]");
            for (User u : students) {
                Student s = (Student) u;
                System.out.printf("%d %s %s\n", s.getId(), s.getName(), s.getEmail());
            }
        }

        if (!teachers.isEmpty()) {
            System.out.println("\n[ TEACHERS ]");
            for (User u : teachers) {
                Teacher t = (Teacher) u;
                System.out.printf("%d %s %s\n", t.getId(), t.getName(), t.getEmail());
            }
        }

        if (!parents.isEmpty()) {
            System.out.println("\n[ PARENTS ]");
            for (User u : parents) {
                Parent p = (Parent) u;
                System.out.printf("%d %s %s\n", p.getId(), p.getName(), p.getEmail());
            }
        }
        System.out.println("\n");
	}

    // Show only students
    public void showStudents()
	{
        // Collection used
		List<User> members = membersService.getAllMembers();
        
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        // Stream filtering
        List<User> students = members.stream().filter(u -> u instanceof Student).collect(Collectors.toList());

        System.out.println("\n--- Students List ---");
        
        if (!students.isEmpty()) {
            System.out.println("\n[ STUDENTS ]");
            for (User u : students) {
                Student s = (Student) u;
                System.out.printf("%d %s %s\n", s.getId(), s.getName(), s.getEmail());
            }
        }

        System.out.println("\n");
	}

    // Get single user details
    public void getUser(long userId) {
        
        // Stream used to find user
        User user = membersService.getAllMembers().stream().filter(u -> u.getId() == userId).findFirst().orElse(null);
        
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        System.out.println("\n--- User Profile ---");
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
        
        // Type checking
        if (user instanceof Student) {
            Student s = (Student) user;
            System.out.println("Age: " + s.getAge());
        } else if (user instanceof Parent) {
            Parent p = (Parent) user;
            System.out.println("Mobile: " + p.getMobileNumber());
        } else if (user instanceof Teacher) {
            Teacher t = (Teacher) user;
            System.out.println("Phone Number: " + t.getPhoneNumber());
        }
        System.out.println("--------------------");
    }
}