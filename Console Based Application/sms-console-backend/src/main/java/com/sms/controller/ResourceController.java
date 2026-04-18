package com.sms.controller;

// Author : Jothika R
/*
 * This Controller handles all resource related functionalities
 * It includes managing ClassRooms and Subjects
 */

import java.util.List;
import java.util.Scanner;

import com.sms.entities.ClassRoom;
import com.sms.entities.Subject;
import com.sms.service.ResourceService;

public class ResourceController {

    // Scanner object for user input
    private final Scanner sc = new Scanner(System.in);
    
    // Service layer object
    private final ResourceService resourceService = new ResourceService();

    // Add new classroom
    public void addClassRoom() {
        System.out.println("\n--- Add New ClassRoom ---");
        try {
            
            // Display existing classrooms
            new ResourceController().showClassRooms();
            
            System.out.print("Enter Class Name (e.g. 10th Grade): ");
            String className = sc.nextLine();
            
            System.out.print("Enter Section: ");
            String section = sc.nextLine();
            
            System.out.print("Enter Capacity: ");
            
            // Conversion method
            int capacity = Integer.parseInt(sc.nextLine());
            
            System.out.print("Enter Academic Year: ");
            String academicYear = sc.nextLine();

            // Creating object
            ClassRoom cr = new ClassRoom(0L, className, section, capacity, academicYear);
            
            // Calling service method
            String result = resourceService.addClassRoom(cr);
            System.out.println(result);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for capacity.");
        }
    }

    // Display all classrooms
    public void showClassRooms() {
        
        // Collection used
        List<ClassRoom> list = resourceService.getAllClassRooms();
        
        if (list.isEmpty()) {
            System.out.println("No ClassRooms found.");
            return;
        }

        System.out.println("\n--- ClassRooms List ---");
        
        // Formatting output in table format
        System.out.printf("%-5s | %-15s | %-10s | %-10s | %-15s\n", "ID", "Class Name", "Section", "Capacity", "Academic Year");
        System.out.println("-".repeat(65));
        
        for (ClassRoom cr : list) {
            System.out.printf("%-5d | %-15s | %-10s | %-10d | %-15s\n",
                    cr.getId(), cr.getClassName(), cr.getSection(), cr.getCapacity(), cr.getAcademicYear());
        }
    }

    // Delete classroom
    public void deleteClassRoom() {
        
        showClassRooms();
        
        System.out.print("Enter ClassRoom ID to delete: ");
        try {
            
            // Conversion method
            long id = Long.parseLong(sc.nextLine());
            
            String result = resourceService.deleteClassRoom(id);
            System.out.println(result);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    // Add new subject
    public void addSubject() {
        System.out.println("\n--- Add New Subject ---");
        try {
            
            System.out.print("Enter Subject Name: ");
            String name = sc.nextLine();
            
            System.out.print("Enter Subject Code: ");
            String code = sc.nextLine();
            
            // Display classrooms before selecting
            showClassRooms();
            
            System.out.print("Enter ClassRoom ID: ");
            
            // Conversion method
            long classId = Long.parseLong(sc.nextLine());

            // Creating object
            Subject sub = new Subject(0L, name, code, classId);
            
            // Calling service method
            String result = resourceService.addSubject(sub);
            System.out.println(result);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! ClassRoom ID must be a number.");
        }
    }

    // Display all subjects
    public void showSubjects() {
        
        // Collection used
        List<Subject> list = resourceService.getAllSubjects();
        
        if (list.isEmpty()) {
            System.out.println("No Subjects found.");
            return;
        }

        System.out.println("\n--- Subjects List ---");
        
        // Formatting output in table format
        System.out.printf("%-5s | %-20s | %-15s | %-10s\n", "ID", "Subject Name", "Subject Code", "Class ID");
        System.out.println("-".repeat(60));
        
        for (Subject s : list) {
            System.out.printf("%-5d | %-20s | %-15s | %-10d\n",
                    s.getSubjectId(), s.getSubjectName(), s.getSubjectCode(), s.getClassId());
        }
    }

    // Delete subject
    public void deleteSubject() {
        
        showSubjects();
        
        System.out.print("Enter Subject ID to delete: ");
        try {
            
            // Conversion method
            long id = Long.parseLong(sc.nextLine());
            
            String result = resourceService.deleteSubject(id);
            System.out.println(result);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }
}