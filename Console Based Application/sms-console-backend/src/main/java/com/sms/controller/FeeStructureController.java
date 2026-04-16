package com.sms.controller;

import java.util.List;
import java.util.Scanner;

import com.sms.entities.FeeStructure;
import com.sms.service.FeeStructureService;

public class FeeStructureController {

    private final Scanner sc = new Scanner(System.in);
    private final FeeStructureService feeStructureService = new FeeStructureService();

    public void addFeeStructure() {
        System.out.println("\n--- Add Fee Structure ---");
        try {
            new ResourceController().showClassRooms();
            System.out.print("Enter Target ClassRoom ID: ");
            long classRoomId = Long.parseLong(sc.nextLine());
            System.out.print("Enter Total Amount: ");
            double totalAmount = Double.parseDouble(sc.nextLine());
            System.out.print("Enter Description (e.g. Tuition + Library): ");
            String description = sc.nextLine();
            System.out.print("Enter Term (e.g. Initial/Annual): ");
            String term = sc.nextLine();

            FeeStructure fs = new FeeStructure(0L, classRoomId, totalAmount, description, term);
            System.out.println(feeStructureService.addFeeStructure(fs));
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    public void editFeeStructure() {
        showFeeStructures();
        System.out.print("Enter Fee Structure ID to edit: ");
        try {
            long id = Long.parseLong(sc.nextLine());
            List<FeeStructure> all = feeStructureService.getAllFeeStructures();
            FeeStructure toEdit = all.stream().filter(f -> f.getId() == id).findFirst().orElse(null);

            if (toEdit == null) {
                System.out.println("Fee Structure not found.");
                return;
            }

            System.out.println("Press Enter to skip updating a specific field.");

            System.out.print("Enter new ClassRoom ID [" + toEdit.getClassRoomId() + "]: ");
            String classIdStr = sc.nextLine();
            if (!classIdStr.trim().isEmpty()) toEdit.setClassRoomId(Long.parseLong(classIdStr));

            System.out.print("Enter new Total Amount [" + toEdit.getTotalAmount() + "]: ");
            String amtStr = sc.nextLine();
            if (!amtStr.trim().isEmpty()) toEdit.setTotalAmount(Double.parseDouble(amtStr));

            System.out.print("Enter new Description [" + toEdit.getDescription() + "]: ");
            String desc = sc.nextLine();
            if (!desc.trim().isEmpty()) toEdit.setDescription(desc);

            System.out.print("Enter new Term [" + toEdit.getTerm() + "]: ");
            String term = sc.nextLine();
            if (!term.trim().isEmpty()) toEdit.setTerm(term);

            System.out.println(feeStructureService.updateFeeStructure(toEdit));

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    public void deleteFeeStructure() {
        showFeeStructures();
        System.out.print("Enter Fee Structure ID to delete: ");
        try {
            long id = Long.parseLong(sc.nextLine());
            System.out.println(feeStructureService.deleteFeeStructure(id));
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    public void showFeeStructures() {
        List<FeeStructure> list = feeStructureService.getAllFeeStructures();
        if (list.isEmpty()) {
            System.out.println("No Fee Structures available.");
            return;
        }

        System.out.println("\n--- Fee Structures ---");
        System.out.printf("%-5s | %-12s | %-12s | %-25s | %-15s\n", "ID", "ClassRoom ID", "Tot Amount", "Description", "Term");
        System.out.println("-".repeat(80));
        for (FeeStructure fs : list) {
            System.out.printf("%-5d | %-12d | %-12.2f | %-25s | %-15s\n",
                    fs.getId(), fs.getClassRoomId(), fs.getTotalAmount(), fs.getDescription(), fs.getTerm());
        }
    }
    
    public void getFeeStructure(long userId) {
        System.out.println("\n--- Your Fee Structures ---");
        com.sms.dao.StudentFeeDAO sfDao = new com.sms.dao.StudentFeeDAO();
        List<com.sms.entities.StudentFee> fees = sfDao.getFeesForStudent(userId);
        
        if (fees.isEmpty()) {
            System.out.println("No fees allocated to you yet.");
            return;
        }

        List<FeeStructure> allStructures = feeStructureService.getAllFeeStructures();

        System.out.printf("%-10s | %-25s | %-12s | %-15s | %-10s\n", "StudentFee", "Description", "Tot Amount", "Term", "Status");
        System.out.println("-".repeat(80));

        for (com.sms.entities.StudentFee sf : fees) {
            FeeStructure match = allStructures.stream().filter(f -> f.getId() == sf.getFeeStructureId()).findFirst().orElse(null);
            if (match != null) {
                System.out.printf("%-10d | %-25s | %-12.2f | %-15s | %-10s\n",
                        sf.getId(), match.getDescription(), match.getTotalAmount(), match.getTerm(), sf.getStatus());
            }
        }

        System.out.print("\nEnter StudentFee ID to pay (or 0 to exit): ");
        try {
            Scanner in = new Scanner(System.in);
            long feeId = Long.parseLong(in.nextLine());
            if(feeId == 0) return;

            com.sms.entities.StudentFee selectedFee = fees.stream().filter(f -> f.getId() == feeId).findFirst().orElse(null);
            if(selectedFee == null) {
                System.out.println("Invalid Selection.");
                return;
            }
            if("PAID".equalsIgnoreCase(selectedFee.getStatus())) {
                System.out.println("This fee is already paid!");
                return;
            }

            FeeStructure structure = allStructures.stream().filter(f -> f.getId() == selectedFee.getFeeStructureId()).findFirst().orElse(null);
            if(structure != null) {
                new PaymentController().processPayment(selectedFee.getId(), structure.getTotalAmount());
            }

        } catch(Exception e) {
            System.out.println("Invalid input!");
        }
    }
}
