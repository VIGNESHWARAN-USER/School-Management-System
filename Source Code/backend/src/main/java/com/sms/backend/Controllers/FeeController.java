package com.sms.backend.Controllers;

import com.sms.backend.Entities.Fee;
import com.sms.backend.Entities.Payment;
import com.sms.backend.Services.FeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fee")
public class FeeController {

    @Autowired
    FeeService feeService;


    // 1️⃣ View Fee Details
    @GetMapping("/details/{studentId}")
    public Fee getFeeDetails(@PathVariable Long studentId)
    {
        return feeService.getFeeDetails(studentId);
    }


    // 2️⃣ Pay Fee
    @PostMapping("/pay")
    public String payFee(@RequestParam Long studentId,
                         @RequestParam double amount,
                         @RequestParam String method)
    {
        return feeService.payFee(studentId, amount, method);
    }


    // 3️⃣ Payment History
    @GetMapping("/history/{studentId}")
    public List<Payment> getHistory(@PathVariable Long studentId)
    {
        return feeService.getPaymentHistory(studentId);
    }


    // 4️⃣ Remaining Balance
    @GetMapping("/balance/{studentId}")
    public double getBalance(@PathVariable Long studentId)
    {
        return feeService.getRemainingBalance(studentId);
    }
}