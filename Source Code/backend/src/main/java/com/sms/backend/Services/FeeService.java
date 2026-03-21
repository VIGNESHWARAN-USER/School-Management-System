package com.sms.backend.Services;

import com.sms.backend.Entities.Fee;
import com.sms.backend.Entities.Payment;
import com.sms.backend.Repositories.FeeRepository;
import com.sms.backend.Repositories.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FeeService {

    @Autowired
    FeeRepository feeRepository;

    @Autowired
    PaymentRepository paymentRepository;


    // 1️⃣ View Fee Details (Parent / Student)
    public Fee getFeeDetails(Long studentId)
    {
        return feeRepository.findByStudentId(studentId);
    }


    // 2️⃣ Pay Fee (Installment supported)
    public String payFee(Long studentId, double amount, String method)
    {
        Fee fee = feeRepository.findByStudentId(studentId);

        if(fee == null)
            return "Fee record not found";

        if(fee.getPendingAmount() <= 0)
            return "Fees already paid";

        // Create payment
        Payment payment = new Payment();
        payment.setPaymentId(System.currentTimeMillis());
        payment.setStudentId(studentId);
        payment.setAmount(amount);
        payment.setPaymentDate(java.time.LocalDate.now().toString());
        payment.setPaymentMethod(method);
        payment.setTransactionId(UUID.randomUUID().toString());

        paymentRepository.save(payment);

        // Update fee
        double newPaid = fee.getPaidAmount() + amount;
        double newPending = fee.getTotalAmount() - newPaid;

        fee.setPaidAmount(newPaid);
        fee.setPendingAmount(newPending);

        // 🔥 STATUS UPDATE
        if(newPending <= 0)
        {
            fee.setStatus("PAID");
        }
        else
        {
            fee.setStatus("PENDING");
        }

        feeRepository.save(fee);

        return "Payment Successful";
    }


    // 3️⃣ Payment History
    public List<Payment> getPaymentHistory(Long studentId)
    {
        return paymentRepository.findByStudentId(studentId);
    }


    // 4️⃣ Remaining Balance
    public double getRemainingBalance(Long studentId)
    {
        Fee fee = feeRepository.findByStudentId(studentId);

        if(fee == null)
            return 0;

        return fee.getPendingAmount();
    }

}