package com.sms.controller;

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
	
    private final java.util.Scanner sc = AppScanner.get();
    private final MembersService membersService = new MembersService();
    private final String DEFAULT_PASSWORD = "1234";

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
        String name = sc.nextLine();
        
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        User newUser = null;

        try {
            if (choice == 1) { 
                System.out.print("Enter Age: ");
                int age = Integer.parseInt(sc.nextLine());
                System.out.print("Enter Address: ");
                String address = sc.nextLine();
                System.out.print("Enter Parent's Email: ");
                String parentEmail = sc.nextLine();
                new ResourceController().showClassRooms();
                System.out.print("Enter Class ID: ");
                long classId = Long.parseLong(sc.nextLine());

                Student s = new Student(0L, name, email, DEFAULT_PASSWORD, "STUDENT", age, address, parentEmail, classId);
                newUser = s;

            } else if (choice == 2) { // Teacher
                System.out.print("Enter Phone Number: ");
                String phone = sc.nextLine();
                new ResourceController().showSubjects();
                System.out.print("Enter Subject ID: ");
                long subjectId = Long.parseLong(sc.nextLine());
                new ResourceController().showClassRooms();
                System.out.print("Enter Class ID: ");
                long classId = Long.parseLong(sc.nextLine());

                Teacher t = new Teacher(0L, name, email, DEFAULT_PASSWORD, "TEACHER", phone, subjectId, classId);
                newUser = t;

            } else if (choice == 3) { // Parent
                System.out.print("Enter Mobile Number: ");
                String mobile = sc.nextLine();
                System.out.print("Enter Address: ");
                String address = sc.nextLine();
                System.out.print("Enter Age: ");
                int age = Integer.parseInt(sc.nextLine());
                showStudents();
                System.out.print("Enter child Student IDs (comma separated): ");
                String idsStr = sc.nextLine();
                java.util.List<Long> childIds = new java.util.ArrayList<>();
                if (!idsStr.trim().isEmpty()) {
                    for (String idStr : idsStr.split(",")) {
                        childIds.add(Long.parseLong(idStr.trim()));
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
	
	public void deleteMember()
	{
		showMembers();
        System.out.print("Enter User ID to delete: ");
        try {
            long userId = Long.parseLong(sc.nextLine());
            String res = membersService.deleteMember(userId);
            System.out.println(res);
        } catch(NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
	}
	
	public void editMember()
	{
		showMembers();
        System.out.print("Enter User ID to edit: ");
        try {
            long userId = Long.parseLong(sc.nextLine());
            List<User> members = membersService.getAllMembers();
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

            if (toEdit instanceof Student) {
                Student s = (Student) toEdit;
                System.out.print("Enter New Age [" + s.getAge() + "]: ");
                String age = sc.nextLine();
                if (!age.trim().isEmpty()) s.setAge(Integer.parseInt(age));

                System.out.print("Enter New Address [" + s.getAddress() + "]: ");
                String address = sc.nextLine();
                if (!address.trim().isEmpty()) s.setAddress(address);

                System.out.print("Enter New Parent's Email [" + s.getParentEmail() + "]: ");
                String pEmail = sc.nextLine();
                if (!pEmail.trim().isEmpty()) s.setParentEmail(pEmail);

                System.out.print("Enter New Class ID [" + s.getClassId() + "]: ");
                String cId = sc.nextLine();
                if (!cId.trim().isEmpty()) s.setClassId(Long.parseLong(cId));

            } else if (toEdit instanceof Teacher) {
                Teacher t = (Teacher) toEdit;
                System.out.print("Enter New Phone Number [" + t.getPhoneNumber() + "]: ");
                String phone = sc.nextLine();
                if (!phone.trim().isEmpty()) t.setPhoneNumber(phone);

                System.out.print("Enter New Subject ID [" + t.getSubjectId() + "]: ");
                String subId = sc.nextLine();
                if (!subId.trim().isEmpty()) t.setSubjectId(Long.parseLong(subId));

                System.out.print("Enter New Class ID [" + t.getClassId() + "]: ");
                String cId = sc.nextLine();
                if (!cId.trim().isEmpty()) t.setClassId(Long.parseLong(cId));

            } else if (toEdit instanceof Parent) {
                Parent p = (Parent) toEdit;
                System.out.print("Enter New Mobile Number [" + p.getMobileNumber() + "]: ");
                String mobile = sc.nextLine();
                if (!mobile.trim().isEmpty()) p.setMobileNumber(mobile);

                System.out.print("Enter New Address [" + p.getAddress() + "]: ");
                String address = sc.nextLine();
                if (!address.trim().isEmpty()) p.setAddress(address);

                System.out.print("Enter New Age [" + p.getAge() + "]: ");
                String age = sc.nextLine();
                if (!age.trim().isEmpty()) p.setAge(Integer.parseInt(age));
            }

            String result = membersService.updateMember(toEdit);
            System.out.println(result);

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        }
	}
	
	public void showMembers()
	{
		List<User> members = membersService.getAllMembers();
        
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        List<User> students = members.stream().filter(u -> u instanceof Student).collect(Collectors.toList());
        List<User> teachers = members.stream().filter(u -> u instanceof Teacher).collect(Collectors.toList());
        List<User> parents = members.stream().filter(u -> u instanceof Parent).collect(Collectors.toList());

        System.out.println("\n--- Members List ---");
        
        if (!students.isEmpty()) {
            System.out.println("\n[ STUDENTS ]");
            System.out.printf("%-5s | %-15s | %-20s | %-4s | %-15s | %-20s | %-8s\n", "ID", "Name", "Email", "Age", "Address", "Parent Email", "Class ID");
            System.out.println("-".repeat(100));
            for (User u : students) {
                Student s = (Student) u;
                System.out.printf("%-5d | %-15s | %-20s | %-4d | %-15s | %-20s | %-8d\n", 
                    s.getId(), s.getName(), s.getEmail(), s.getAge(), s.getAddress(), s.getParentEmail(), s.getClassId());
            }
        }

        if (!teachers.isEmpty()) {
            System.out.println("\n[ TEACHERS ]");
            System.out.printf("%-5s | %-15s | %-20s | %-15s | %-10s | %-8s\n", "ID", "Name", "Email", "Phone", "Subject ID", "Class ID");
            System.out.println("-".repeat(90));
            for (User u : teachers) {
                Teacher t = (Teacher) u;
                System.out.printf("%-5d | %-15s | %-20s | %-15s | %-10s | %-8s\n", 
                    t.getId(), t.getName(), t.getEmail(), t.getPhoneNumber(), String.valueOf(t.getSubjectId()), String.valueOf(t.getClassId()));
            }
        }

        if (!parents.isEmpty()) {
            System.out.println("\n[ PARENTS ]");
            System.out.printf("%-5s | %-15s | %-20s | %-15s | %-4s | %-15s\n", "ID", "Name", "Email", "Mobile", "Age", "Address");
            System.out.println("-".repeat(90));
            for (User u : parents) {
                Parent p = (Parent) u;
                System.out.printf("%-5d | %-15s | %-20s | %-15s | %-4d | %-15s\n", 
                    p.getId(), p.getName(), p.getEmail(), p.getMobileNumber(), p.getAge(), p.getAddress());
            }
        }
        System.out.println("\n");
	}

    public void showStudents()
	{
		List<User> members = membersService.getAllMembers();
        
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        List<User> students = members.stream().filter(u -> u instanceof Student).collect(Collectors.toList());

        System.out.println("\n--- Students List ---");
        
        if (!students.isEmpty()) {
            System.out.println("\n[ STUDENTS ]");
            System.out.printf("%-5s | %-15s | %-20s | %-4s | %-15s | %-20s | %-8s\n", "ID", "Name", "Email", "Age", "Address", "Parent Email", "Class ID");
            System.out.println("-".repeat(100));
            for (User u : students) {
                Student s = (Student) u;
                System.out.printf("%-5d | %-15s | %-20s | %-4d | %-15s | %-20s | %-8d\n", 
                    s.getId(), s.getName(), s.getEmail(), s.getAge(), s.getAddress(), s.getParentEmail(), s.getClassId());
            }
        }

        System.out.println("\n");
	}

    public void getUser(long userId) {
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
        
        if (user instanceof Student) {
            Student s = (Student) user;
            System.out.println("Age: " + s.getAge());
            System.out.println("Address: " + s.getAddress());
            System.out.println("Parent Email: " + s.getParentEmail());
            System.out.println("Class ID: " + s.getClassId());
        } else if (user instanceof Parent) {
            Parent p = (Parent) user;
            System.out.println("Age: " + p.getAge());
            System.out.println("Mobile: " + p.getMobileNumber());
            System.out.println("Address: " + p.getAddress());
            System.out.println("Linked Children IDs: " + p.getChildIds());
        } else if (user instanceof Teacher) {
            Teacher t = (Teacher) user;
            System.out.println("Phone Number: " + t.getPhoneNumber());
            System.out.println("Subject ID: " + t.getSubjectId());
            System.out.println("Class ID: " + t.getClassId());
        }
        System.out.println("--------------------");
    }
}
