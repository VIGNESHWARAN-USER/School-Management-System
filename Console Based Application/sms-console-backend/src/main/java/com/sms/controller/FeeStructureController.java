package com.sms.controller;

/*
 * Author : Shobana V
 * This Controller handles all Fee Structure related functionalities
 * It includes adding, updating, deleting and viewing fee structures
 */

import java.util.List;
import java.util.Scanner;

import com.sms.entities.FeeStructure;
import com.sms.service.FeeStructureService;
import com.sms.util.AppScanner;

public class FeeStructureController {

    // Getting scanner object
    private final Scanner sc = AppScanner.get();

    // Creating Service object
    private final FeeStructureService feeStructureService = new FeeStructureService();

    // Add new fee structure
    public void addFeeStructure() {
        System.out.println("\n--- Add Fee Structure ---");
        try {
            // Showing available classrooms before assigning fee
            new ResourceController().showClassRooms();

            System.out.print("Enter Target ClassRoom ID: ");
            
            // Conversion method
            long classRoomId = Long.parseLong(sc.nextLine());

            System.out.print("Enter Total Amount: ");
            
            // Conversion method
            double totalAmount = Double.parseDouble(sc.nextLine());

            System.out.print("Enter Description (e.g. Tuition + Library): ");
            
            // String handling
            String description = sc.nextLine();

            System.out.print("Enter Term (e.g. Initial/Annual): ");
            
            // String handling
            String term = sc.nextLine();

            // Creating FeeStructure object
            FeeStructure fs = new FeeStructure(0L, classRoomId, totalAmount, description, term);

            System.out.println(feeStructureService.addFeeStructure(fs));

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    // Edit existing fee structure
    public void editFeeStructure() {

        showFeeStructures();
        System.out.print("Enter Fee Structure ID to edit: ");

        try {
            // Conversion method
            long id = Long.parseLong(sc.nextLine());

            // Collection used
            List<FeeStructure> all = feeStructureService.getAllFeeStructures();

            // Using stream to find matching record
            FeeStructure toEdit = all.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (toEdit == null) {
                System.out.println("Fee Structure not found.");
                return;
            }

            System.out.println("Press Enter to skip updating a specific field.");

            // String handling with conditional update
            System.out.print("Enter new ClassRoom ID [" + toEdit.getClassRoomId() + "]: ");
            String classIdStr = sc.nextLine();
            if (!classIdStr.trim().isEmpty())
                toEdit.setClassRoomId(Long.parseLong(classIdStr));

            System.out.print("Enter new Total Amount [" + toEdit.getTotalAmount() + "]: ");
            String amtStr = sc.nextLine();
            if (!amtStr.trim().isEmpty())
                toEdit.setTotalAmount(Double.parseDouble(amtStr));

            System.out.print("Enter new Description [" + toEdit.getDescription() + "]: ");
            String desc = sc.nextLine();
            if (!desc.trim().isEmpty())
                toEdit.setDescription(desc);

            System.out.print("Enter new Term [" + toEdit.getTerm() + "]: ");
            String term = sc.nextLine();
            if (!term.trim().isEmpty())
                toEdit.setTerm(term);

            System.out.println(feeStructureService.updateFeeStructure(toEdit));

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    // Delete fee structure
    public void deleteFeeStructure() {

        showFeeStructures();
        System.out.print("Enter Fee Structure ID to delete: ");

        try {
            // Conversion method
            long id = Long.parseLong(sc.nextLine());

            System.out.println(feeStructureService.deleteFeeStructure(id));

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    // Display all fee structures
    public void showFeeStructures() {

        // Collection used
        List<FeeStructure> list = feeStructureService.getAllFeeStructures();

        if (list.isEmpty()) {
            System.out.println("No Fee Structures available.");
            return;
        }

        System.out.println("\n--- Fee Structures ---");

        // Formatting output in table format
        System.out.printf("%-5s | %-12s | %-12s | %-25s | %-15s\n",
                "ID", "ClassRoom ID", "Tot Amount", "Description", "Term");

        System.out.println("-".repeat(80));

        // Iterating collection
        for (FeeStructure fs : list) {
            System.out.printf("%-5d | %-12d | %-12.2f | %-25s | %-15s\n",
                    fs.getId(),
                    fs.getClassRoomId(),
                    fs.getTotalAmount(),
                    fs.getDescription(),
                    fs.getTerm());
        }
    }

    // View student fee structure and allow payment
    public void getFeeStructure(long userId, boolean allowPay) {

        System.out.println("\n--- Your Fee Structures ---");

        // DAO object
        com.sms.dao.StudentFeeDAO sfDao = new com.sms.dao.StudentFeeDAO();

        // Collection used
        java.util.List<com.sms.entities.StudentFee> fees = sfDao.getFeesForStudent(userId);

        if (fees.isEmpty()) {
            System.out.println("No fees allocated yet.");
            return;
        }

        // Collection used
        List<FeeStructure> allStructures = feeStructureService.getAllFeeStructures();

        // Formatting output
        System.out.printf("%-10s | %-25s | %-12s | %-15s | %-10s\n",
                "StudentFee", "Description", "Tot Amount", "Term", "Status");

        System.out.println("-".repeat(80));

        // Iterating collection
        for (com.sms.entities.StudentFee sf : fees) {

            // Matching FeeStructure using stream
            FeeStructure match = allStructures.stream()
                    .filter(f -> f.getId() == sf.getFeeStructureId())
                    .findFirst()
                    .orElse(null);

            if (match != null) {
                System.out.printf("%-10d | %-25s | %-12.2f | %-15s | %-10s\n",
                        sf.getId(),
                        match.getDescription(),
                        match.getTotalAmount(),
                        match.getTerm(),
                        sf.getStatus());
            }
        }

        if (!allowPay) return;

        System.out.print("\nEnter StudentFee ID to pay (or 0 to exit): ");

        try {
            // Conversion method
            long feeId = Long.parseLong(sc.nextLine());

            if (feeId == 0) return;

            // Finding selected fee using stream
            com.sms.entities.StudentFee selectedFee = fees.stream()
                    .filter(f -> f.getId() == feeId)
                    .findFirst()
                    .orElse(null);

            if (selectedFee == null) {
                System.out.println("Invalid Selection.");
                return;
            }

            // String handling for status check
            if ("PAID".equalsIgnoreCase(selectedFee.getStatus())) {
                System.out.println("This fee is already paid!");
                return;
            }

            // Fetching matching fee structure
            FeeStructure structure = allStructures.stream()
                    .filter(f -> f.getId() == selectedFee.getFeeStructureId())
                    .findFirst()
                    .orElse(null);

            if (structure != null) {
                new PaymentController().processPayment(
                        selectedFee.getId(),
                        structure.getTotalAmount()
                );
            }

        } catch (Exception e) {
            System.out.println("Invalid input!");
        }
    }
}