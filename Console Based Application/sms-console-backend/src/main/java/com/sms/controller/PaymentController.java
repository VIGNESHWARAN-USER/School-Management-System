package com.sms.controller;

// Author : Reshma K
/*
 * This Controller handles all payment related functionalities
 * It includes processing different payment methods and viewing transaction history
 */

import java.util.Scanner;

import com.sms.dao.PaymentDAO;
import com.sms.dao.StudentFeeDAO;
import com.sms.entities.CreditCardPayment;
import com.sms.entities.NetBankingPayment;
import com.sms.entities.OfflinePayment;
import com.sms.entities.Payment;
import com.sms.entities.UPIPayment;
import com.sms.util.AppScanner;

public class PaymentController {

    // Getting scanner object
    private final Scanner sc = AppScanner.get();
    
    // DAO objects for database interaction
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final StudentFeeDAO studentFeeDAO = new StudentFeeDAO();

    // Entry point to process payment
    public void processPayment(long studentFeeId, double amountToPay) {
        
        System.out.println("\n--- Payment Gateway ---");
        
        // Displaying amount
        System.out.printf("  Amount Due : Rs. %.2f%n", amountToPay);
        
        // Showing payment options
        System.out.println("  Select Payment Method:");
        System.out.println("    1. UPI");
        System.out.println("    2. Credit Card");
        System.out.println("    3. Net Banking");
        System.out.println("    4. Offline / Cash");
        System.out.println("    0. Cancel");
        System.out.print("  Enter choice: ");

        int choice;
        try {
            // Conversion method
            choice = Integer.parseInt(sc.nextLine().trim());
        }
        catch (Exception e) {
            System.out.println("Invalid input. Payment cancelled.");
            return;
        }

        // Polymorphism used here to create different payment types
        Payment payment;
        switch (choice) {
            case 1: payment = collectUPI(amountToPay); break;
            case 2: payment = collectCreditCard(amountToPay); break;
            case 3: payment = collectNetBanking(amountToPay); break;
            case 4: payment = collectOffline(amountToPay); break;
            case 0: System.out.println("  Payment cancelled."); return;
            default: System.out.println("  Invalid choice. Payment cancelled."); return;
        }

        // If user cancels input
        if (payment == null) return;

        // Validation logic
        String error = payment.validate();
        if (error != null) {
            System.out.println("\n  Validation Failed: " + error);
            System.out.println("  Payment cancelled. Please try again.");
            return;
        }

        // Simulating payment processing
        System.out.println("\n  Processing payment, please wait...");
        boolean success = payment.process();

        if (success) {
            
            // Saving payment details in database
            long paymentId = paymentDAO.createPayment(
                payment.getAmountPaid(),
                payment.getPaymentMethod(),
                payment.getRemarks()
            );

            if (paymentId != -1) {
                
                // Updating student fee status
                studentFeeDAO.updateFeeStatus(studentFeeId, "PAID", paymentId);

                System.out.println("\n  Payment Successful!");
                System.out.printf("  Receipt ID  : %d%n", paymentId);
                System.out.printf("  Method      : %s%n", payment.getPaymentMethod());
                System.out.printf("  Amount Paid : Rs. %.2f%n", payment.getAmountPaid());
            } else {
                System.out.println("  Payment processed but failed to save record. Contact admin.");
            }
        } else {
            System.out.println("\n  Payment Failed! Please try again.");
        }
    }

    // Collect UPI payment details
    private Payment collectUPI(double amount) {
        
        System.out.println("\n  --- UPI Payment ---");
        System.out.print("  Enter UPI ID (e.g. john@okicici): ");
        
        // String handling
        String upiId = sc.nextLine().trim();
        
        if (upiId.isEmpty()) {
            System.out.println("  Cancelled.");
            return null;
        }
        
        return new UPIPayment(amount, upiId);
    }

    // Collect Credit Card details
    private Payment collectCreditCard(double amount) {
        
        System.out.println("\n  --- Credit Card Payment ---");
        
        System.out.print("  Card Number (16 digits, spaces allowed): ");
        String cardNo = sc.nextLine().trim();

        System.out.print("  Card Holder Name: ");
        String name = sc.nextLine().trim();

        System.out.print("  Expiry Date (MM/YY): ");
        String expiry = sc.nextLine().trim();

        System.out.print("  CVV (3 digits): ");
        String cvv = sc.nextLine().trim();

        return new CreditCardPayment(amount, cardNo, name, expiry, cvv);
    }

    // Collect Net Banking details
    private Payment collectNetBanking(double amount) {
        
        System.out.println("\n  --- Net Banking Payment ---");
        
        System.out.print("  Bank Name: ");
        String bank = sc.nextLine().trim();

        System.out.print("  Account Number: ");
        String acc = sc.nextLine().trim();

        System.out.print("  IFSC Code (e.g. SBIN0001234): ");
        String ifsc = sc.nextLine().trim();

        return new NetBankingPayment(amount, bank, acc, ifsc);
    }

    // Collect Offline payment details
    private Payment collectOffline(double amount) {
        
        System.out.println("\n  --- Offline / Cash Payment ---");
        
        System.out.print("  Receipt Reference (from cashier): ");
        String ref = sc.nextLine().trim();

        System.out.printf("  Cash Tendered (amount due: %.2f): ", amount);
        
        double tendered;
        try {
            // Conversion method
            tendered = Double.parseDouble(sc.nextLine().trim());
        }
        catch (Exception e) {
            System.out.println("  Invalid amount. Cancelled.");
            return null;
        }

        return new OfflinePayment(amount, ref, tendered);
    }

    // Display transaction history
    public void getPayment(long studentId) {
        
        System.out.println("\n--- Transaction History ---");
        
        try {
            // Database connection
            java.sql.Connection con = com.sms.util.DatabaseConfig.getConnection();

            // SQL query
            String query = "SELECT sf.id as fee_id, p.id as pay_id, p.amount_paid, p.payment_date, p.status, p.payment_method, p.remarks " +
                           "FROM payments p " +
                           "JOIN student_fees sf ON sf.payment_id = p.id " +
                           "WHERE sf.student_id = ?";

            java.sql.PreparedStatement ps = con.prepareStatement(query);
            
            // Setting parameter
            ps.setLong(1, studentId);

            java.sql.ResultSet rs = ps.executeQuery();

            // Formatting output
            System.out.printf("%-8s | %-10s | %-10s | %-22s | %-14s | %-8s | %-s%n",
                "Fee ID", "Receipt ID", "Amount", "Date", "Method", "Status", "Remarks");
            
            System.out.println("-".repeat(105));

            boolean found = false;

            // Iterating result set
            while (rs.next()) {
                found = true;
                System.out.printf("%-8d | %-10d | %-10.2f | %-22s | %-14s | %-8s | %-s%n",
                    rs.getLong("fee_id"),
                    rs.getLong("pay_id"),
                    rs.getDouble("amount_paid"),
                    rs.getTimestamp("payment_date").toString(),
                    rs.getString("payment_method"),
                    rs.getString("status"),
                    rs.getString("remarks"));
            }

            if (!found) System.out.println("  No transactions found.");

        } catch (Exception e) {
            System.out.println("Error retrieving transactions: " + e.getMessage());
        }
    }
}