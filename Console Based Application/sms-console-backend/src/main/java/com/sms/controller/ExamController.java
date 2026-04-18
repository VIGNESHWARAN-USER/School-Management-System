package com.sms.controller;

import java.util.List;
import java.util.Scanner;
import com.sms.entities.Exam;
import com.sms.entities.Grade;
import com.sms.entities.Student;
import com.sms.entities.User;
import com.sms.service.ExamService;
import com.sms.service.MembersService;
import com.sms.util.AppScanner;

public class ExamController {

    private final Scanner sc = AppScanner.get();
    private final ExamService examService = new ExamService();
    private final MembersService membersService = new MembersService();

    // --- Admin Functionality ---

    public void adminExamMenu() {
        while (true) {
            System.out.println("\n--- EXAM MANAGEMENT (ADMIN) ---");
            System.out.println("1. Add Exam");
            System.out.println("2. Edit Exam");
            System.out.println("3. Delete Exam");
            System.out.println("4. View All Exams");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");
            int choice = 0;
            try { choice = Integer.parseInt(sc.nextLine()); } catch (Exception e) { continue; }

            switch (choice) {
                case 1: addExam(); break;
                case 2: editExam(); break;
                case 3: deleteExam(); break;
                case 4: viewAllExams(); break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addExam() {
        System.out.print("Enter Exam Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Description: ");
        String desc = sc.nextLine();
        System.out.print("Enter Subject ID: ");
        long subId = Long.parseLong(sc.nextLine());
        System.out.print("Enter Class ID: ");
        long classId = Long.parseLong(sc.nextLine());
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter Max Marks: ");
        double maxMarks = Double.parseDouble(sc.nextLine());

        Exam exam = new Exam(0, name, desc, subId, classId, date, maxMarks);
        System.out.println(examService.addExam(exam));
    }

    private void editExam() {
        System.out.print("Enter Exam ID to edit: ");
        long id = Long.parseLong(sc.nextLine());
        Exam exam = examService.getExamById(id);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }

        System.out.print("Enter New Name (leave blank to keep current): ");
        String name = sc.nextLine();
        if (!name.isEmpty()) exam.setName(name);

        System.out.print("Enter New Description (leave blank to keep current): ");
        String desc = sc.nextLine();
        if (!desc.isEmpty()) exam.setDescription(desc);

        System.out.print("Enter New Date (YYYY-MM-DD) (leave blank to keep current): ");
        String date = sc.nextLine();
        if (!date.isEmpty()) exam.setExamDate(date);

        System.out.print("Enter New Max Marks (or 0 to keep current): ");
        double max = Double.parseDouble(sc.nextLine());
        if (max > 0) exam.setMaxMarks(max);

        System.out.println(examService.updateExam(exam));
    }

    private void deleteExam() {
        System.out.print("Enter Exam ID to delete: ");
        long id = Long.parseLong(sc.nextLine());
        System.out.println(examService.deleteExam(id));
    }

    private void viewAllExams() {
        List<Exam> exams = examService.getAllExams();
        if (exams.isEmpty()) {
            System.out.println("No exams found.");
            return;
        }
        System.out.printf("%-5s | %-20s | %-10s | %-10s | %-10s%n", "ID", "Name", "SubID", "ClassID", "Date");
        for (Exam e : exams) {
            System.out.printf("%-5d | %-20s | %-10d | %-10d | %-10s%n", e.getId(), e.getName(), e.getSubjectId(), e.getClassRoomId(), e.getExamDate());
        }
    }

    // --- Teacher Functionality ---

    public void teacherGradingMenu(long teacherClassId) {
        while (true) {
            System.out.println("\n--- GRADING MANAGEMENT (TEACHER) ---");
            System.out.println("1. View Exams for My Class");
            System.out.println("2. Assign Grades for an Exam");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            int choice = 0;
            try { choice = Integer.parseInt(sc.nextLine()); } catch (Exception e) { continue; }

            switch (choice) {
                case 1: viewExamsByClass(teacherClassId); break;
                case 2: assignGrades(teacherClassId); break;
                case 3: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void viewExamsByClass(long classId) {
        List<Exam> exams = examService.getExamsByClass(classId);
        if (exams.isEmpty()) {
            System.out.println("No exams found for your class.");
            return;
        }
        System.out.printf("%-5s | %-20s | %-10s | %-10s%n", "ID", "Name", "Date", "Max Marks");
        for (Exam e : exams) {
            System.out.printf("%-5d | %-20s | %-10s | %-10.2f%n", e.getId(), e.getName(), e.getExamDate(), e.getMaxMarks());
        }
    }

    private void assignGrades(long classId) {
        System.out.print("Enter Exam ID: ");
        long examId = Long.parseLong(sc.nextLine());
        Exam exam = examService.getExamById(examId);
        if (exam == null || exam.getClassRoomId() != classId) {
            System.out.println("Invalid Exam ID or not authorized for this class.");
            return;
        }

        // Get students in this class
        List<User> allMembers = membersService.getAllMembers();
        System.out.println("\nStudents in your class:");
        for (User u : allMembers) {
            if (u instanceof Student && ((Student) u).getClassId() == classId) {
                System.out.println("ID: " + u.getId() + " | Name: " + u.getName());
            }
        }

        System.out.print("Enter Student ID to grade: ");
        long studentId = Long.parseLong(sc.nextLine());
        System.out.print("Enter Marks Obtained (Max: " + exam.getMaxMarks() + "): ");
        double marks = Double.parseDouble(sc.nextLine());
        System.out.print("Enter Remarks: ");
        String remarks = sc.nextLine();

        Grade grade = new Grade(0, studentId, examId, marks, "", remarks);
        System.out.println(examService.assignGrade(grade));
    }

    // --- Student/Parent Functionality ---

    public void studentResultsMenu(long studentId) {
        System.out.println("\n--- YOUR EXAM RESULTS ---");
        List<Grade> grades = examService.getStudentResults(studentId);
        displayGrades(grades);
    }

    public void parentResultsMenu(List<Long> childIds) {
        System.out.println("\n--- CHILDREN'S EXAM RESULTS ---");
        for (Long childId : childIds) {
            User u = membersService.getAllMembers().stream().filter(m -> m.getId() == childId).findFirst().orElse(null);
            System.out.println("Results for: " + (u != null ? u.getName() : "ID " + childId));
            List<Grade> grades = examService.getStudentResults(childId);
            displayGrades(grades);
            System.out.println("---------------------------");
        }
    }

    private void displayGrades(List<Grade> grades) {
        if (grades.isEmpty()) {
            System.out.println("No results available.");
            return;
        }
        System.out.printf("%-20s | %-10s | %-10s | %-10s | %-15s%n", "Exam", "Marks", "Max", "Grade", "Remarks");
        for (Grade g : grades) {
            Exam e = examService.getExamById(g.getExamId());
            String examName = (e != null) ? e.getName() : "Unknown";
            double max = (e != null) ? e.getMaxMarks() : 0.0;
            System.out.printf("%-20s | %-10.2f | %-10.2f | %-10s | %-15s%n", examName, g.getMarksObtained(), max, g.getLetterGrade(), g.getRemarks());
        }
    }
}
