package com.sms.backend.Controllers;

import com.sms.backend.DTO.FeeStructureDTO;
import com.sms.backend.DTO.InstallmentPlanDTO;
import com.sms.backend.DTO.PaymentRequestDTO;
import com.sms.backend.Entities.FeeStructure;
import com.sms.backend.Services.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fees")
@CrossOrigin(origins = "*")
public class FeeController {

    @Autowired
    private FeeService feeService;

    @PostMapping("/create-structure")
    public ResponseEntity<?> createStructure(@RequestBody FeeStructureDTO dto) {
        try {
            return feeService.createFeeStructure(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating fee structure: " + e.getMessage());
        }
    }

    @GetMapping("/structure/{classId}")
    public ResponseEntity<?> getStructure(@PathVariable String classId)
    {
        try {
            return feeService.getStructure(classId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching fee structure: " + e.getMessage());
        }
    }

    @GetMapping("/all-structures")
    public ResponseEntity<?> getAllStructure()
    {
        try {
            return feeService.getAllStructure();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching fee structure: " + e.getMessage());
        }
    }

    @GetMapping("/get-installments/{id}")
    public ResponseEntity<?> getInstallments(@PathVariable Long id)
    {
        try {
            return feeService.getInstallments(id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching fee structure: " + e.getMessage());
        }
    }

    @PostMapping("/save-installments")
    public ResponseEntity<?> saveInstallments(@RequestBody InstallmentPlanDTO dto) {
        // Validate percentage sum is 100
        double sum = dto.getInstallments().stream().mapToDouble(i -> i.getPercentage()).sum();
        if (Math.abs(sum - 100.0) > 0.1) {
            return ResponseEntity.badRequest().body("Total percentage must be 100%");}

        System.out.println(dto);
        feeService.saveAndApplyInstallments(dto);
        return ResponseEntity.ok("Installment plan applied to all students in class.");
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentDashboard(@PathVariable Long studentId) {
        try {
            System.out.println("Hi");
            return ResponseEntity.ok(feeService.getStudentFeeDashboard(studentId));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequestDTO request) {
        try {
            return ResponseEntity.ok(feeService.processPayment(request));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Payment Failed: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/transactions")
    public ResponseEntity<?> getStudentTransactions(@PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(feeService.getStudentTransactions(studentId));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }


}