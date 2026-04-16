package com.sms.controller;

import java.util.Scanner;
import com.sms.dao.PaymentDAO;
import com.sms.dao.StudentFeeDAO;

public class PaymentController {
    
    private Scanner sc = new Scanner(System.in);
    private PaymentDAO paymentDAO = new PaymentDAO();
    private StudentFeeDAO studentFeeDAO = new StudentFeeDAO();

    public void processPayment(long studentFeeId, double amountToPay) {
        System.out.println("\n--- Processing Payment ---");
        System.out.println("Amount to Pay: $" + amountToPay);
        System.out.println("Please select a Payment Method:");
        System.out.println("1. Credit Card");
        System.out.println("2. Net Banking");
        System.out.println("3. UPI");
        System.out.println("4. Offline Cash");
        System.out.print("Enter choice: ");
        
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch(Exception e) {
            System.out.println("Invalid input. Cancelling payment.");
            return;
        }

        String method = "";
        switch (choice) {
            case 1: method = "CREDIT_CARD"; break;
            case 2: method = "NET_BANKING"; break;
            case 3: method = "UPI"; break;
            case 4: method = "OFFLINE"; break;
            default:
                System.out.println("Invalid method. Cancelling.");
                return;
        }

        System.out.println("Contacting payment gateway... Mocking processing...");
        
        try { Thread.sleep(1500); } catch(Exception e) {}
        
        long paymentId = paymentDAO.createPayment(amountToPay, method, "Paid via " + method);
        if (paymentId != -1) {
            boolean updated = studentFeeDAO.updateFeeStatus(studentFeeId, "PAID", paymentId);
            if (updated) {
                System.out.println("Payment Successful! Receipt ID: " + paymentId);
            } else {
                System.out.println("Payment succeeded but failed to update fee status. Please contact Admin.");
            }
        } else {
            System.out.println("Payment Failed! Please try again.");
        }
    }
}
