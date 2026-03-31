package com.sms.backend.Services;

import com.sms.backend.DTO.*;
import com.sms.backend.Entities.*;
import com.sms.backend.Enum.*;
import com.sms.backend.Repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FeeService {

    @Autowired private FeeStructureRepository feeStructureRepository;
    @Autowired private FeeComponentRepository feeComponentRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private StudentFeeRepository studentFeeRepository;
    @Autowired private InstallmentRepository installmentRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private EmailService emailService;
    @Autowired
    private ClassRoomRepository classRoomRepository;

    //  AUTO FEE DUE REMINDER

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendDueReminder() {

        List<Installment> installments = installmentRepository.findAll();

        for (Installment inst : installments) {

            if (inst.getStatus() == PaymentStatus.PENDING &&
                    inst.getDueDate().minusDays(1).equals(LocalDate.now())) {

                StudentFee fee = inst.getStudentFee();
                if (fee == null) continue;

                Student student = studentRepository.findById(fee.getStudentId()).orElse(null);
                if (student == null) continue;

                String parentEmail = student.getParentEmail();
                if (parentEmail == null) continue;

                String body = "<h3>Fee Reminder</h3>" +
                        "<p><b>Student:</b> " + student.getName() + "</p>" +
                        "<p><b>Installment:</b> " + inst.getInstallmentName() + "</p>" +
                        "<p><b>Due Date:</b> " + inst.getDueDate() + "</p>" +
                        "<p><b>Amount:</b> ₹" + inst.getAmount() + "</p>";

                try {
                    emailService.sendHtmlEmail(parentEmail, "Fee Due Reminder", body);
                } catch (Exception e) {
                    System.out.println("Reminder failed");
                }
            }
        }
    }

    // CREATE / UPDATE FEE STRUCTURE

    @Transactional
    public ResponseEntity<?> createFeeStructure(FeeStructureDTO dto) {

        FeeStructure structure = feeStructureRepository
                .findByClassIdAndAcademicYear(dto.getClassId(), dto.getAcademicYear());

        if (structure == null) {
            structure = new FeeStructure();
            structure.setClassId(dto.getClassId());
            structure.setAcademicYear(dto.getAcademicYear());
        }

        List<FeeComponent> components = new ArrayList<>();
        double total = 0;

        for (FeeComponentDTO c : dto.getComponents()) {

            FeeComponent comp = new FeeComponent();
            comp.setComponentName(FeeComponentType.valueOf(c.getComponentName()));
            comp.setDescription(c.getDescription());
            comp.setAmount(c.getAmount());
            comp.setTaxPercentage(c.getTaxPercentage());

            double tax = (c.getAmount() * c.getTaxPercentage()) / 100;
            total += c.getAmount() + tax;

            comp.setFeeStructure(structure);
            components.add(comp);
        }

        structure.setComponents(components);
        structure.setTotalAmount(BigDecimal.valueOf(total));

        feeComponentRepository.saveAll(components);
        feeStructureRepository.save(structure);

        return ResponseEntity.ok(structure);
    }

    // GET SINGLE STRUCTURE

    public ResponseEntity<?> getStructure(String classId, String year) {

        FeeStructure fs = feeStructureRepository.findByClassIdAndAcademicYear(classId, year);

        FeeStructureDTO dto = new FeeStructureDTO();
        dto.setClassId(fs.getClassId());
        dto.setAcademicYear(fs.getAcademicYear());

        List<FeeComponentDTO> list = fs.getComponents().stream().map(c -> {
            FeeComponentDTO d = new FeeComponentDTO();
            d.setComponentName(c.getComponentName().toString());
            d.setAmount(c.getAmount());
            d.setDescription(c.getDescription());
            d.setTaxPercentage(c.getTaxPercentage());
            return d;
        }).toList();

        dto.setComponents(list);

        return ResponseEntity.ok(dto);
    }


    // GET ALL STRUCTURES

    public ResponseEntity<?> getAllStructure() {

        List<FeeStructureDTO> list = feeStructureRepository.findAll().stream().map(fs -> {
            FeeStructureDTO dto = new FeeStructureDTO();
            dto.setClassId(fs.getClassId());
            dto.setAcademicYear(fs.getAcademicYear());
            dto.setFeeStructureId(fs.getId());
            dto.setTotalAmount(fs.getTotalAmount());
            return dto;
        }).toList();

        return ResponseEntity.ok(list);
    }

    // APPLY INSTALLMENTS

    @Transactional
    public void saveAndApplyInstallments(InstallmentPlanDTO dto) {

        FeeStructure structure = feeStructureRepository.findById(dto.getFeeStructureId())
                .orElseThrow(() -> new RuntimeException("Structure not found"));

        List<Student> students = studentRepository.findAllByClassRoom(classRoomRepository.findById(Long.valueOf(structure.getClassId())).orElse(null));

        for (Student student : students) {

            StudentFee fee = studentFeeRepository.findByStudentId(student.getId());
            if (fee == null) fee = new StudentFee();

            fee.setStudentId(student.getId());
            fee.setFeeStructure(structure);
            fee.setAmountPaid(BigDecimal.ZERO);
            fee.setRemainingBalance(structure.getTotalAmount());
            fee.setStatus(FeeStatus.PENDING);

            StudentFee saved = studentFeeRepository.save(fee);

            installmentRepository.deleteByStudentFee(saved);

            for (InstallmentItemDTO i : dto.getInstallments()) {

                Installment inst = new Installment();
                inst.setInstallmentName(i.getInstallmentName());
                inst.setDueDate(LocalDate.parse(i.getDueDate()));
                inst.setStatus(PaymentStatus.PENDING);
                inst.setStudentFee(saved);

                BigDecimal amt = structure.getTotalAmount()
                        .multiply(BigDecimal.valueOf(i.getPercentage()))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                inst.setAmount(amt);

                installmentRepository.save(inst);
            }
        }
    }

    // GET INSTALLMENTS
    public ResponseEntity<?> getInstallments(Long id) {

        StudentFee fee = studentFeeRepository
                .findAllByFeeStructure(feeStructureRepository.findById(id).orElse(null))
                .getFirst();

        List<InstallmentItemDTO> list = fee.getInstallments().stream().map(inst -> {

            InstallmentItemDTO dto = new InstallmentItemDTO();
            dto.setInstallmentName(inst.getInstallmentName());
            dto.setDueDate(inst.getDueDate().toString());

            double percent = inst.getAmount()
                    .divide(fee.getFeeStructure().getTotalAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).doubleValue();

            dto.setPercentage(percent);

            return dto;
        }).toList();

        return ResponseEntity.ok(list);
    }


    // DASHBOARD

    public FeeDashboardDTO getStudentFeeDashboard(Long studentId) {

        StudentFee fee = studentFeeRepository.findByStudentId(studentId);
        if (fee == null) throw new RuntimeException("No record");

        FeeDashboardDTO dto = new FeeDashboardDTO();
        dto.setStudentId(studentId);
        dto.setAmountPaid(fee.getAmountPaid());
        dto.setRemainingBalance(fee.getRemainingBalance());
        dto.setOverallStatus(fee.getStatus().toString());

        dto.setBreakdown(fee.getFeeStructure().getComponents().stream().map(feeComponent -> {
            ComponentDTO componentDTO = new ComponentDTO();
            componentDTO.setAmount(feeComponent.getAmount());
            componentDTO.setName(String.valueOf(feeComponent.getComponentName()));
            componentDTO.setDescription(feeComponent.getDescription());
            componentDTO.setTaxPercentage(feeComponent.getTaxPercentage());
            return componentDTO;
        }).toList());

        dto.setInstallments(fee.getInstallments().stream().map(i -> {
            InstallmentDTO d = new InstallmentDTO();
            d.setId(i.getId());
            d.setName(i.getInstallmentName());
            d.setAmount(i.getAmount());
            d.setDueDate(i.getDueDate().toString());
            d.setStatus(i.getStatus().toString());
            return d;
        }).toList());

        return dto;
    }

    //  PAYMENT + EMAIL
    @Transactional
    public PaymentTransaction processPayment(PaymentRequestDTO request) {

        StudentFee fee = studentFeeRepository.findByStudentId(request.getStudentId());
        if (fee == null) throw new RuntimeException("Not found");

        Installment inst = installmentRepository.findById(request.getInstallmentId())
                .orElseThrow(() -> new RuntimeException("Installment not found"));

        if (inst.getStatus() == PaymentStatus.PAID)
            throw new RuntimeException("Already paid");

        inst.setStatus(PaymentStatus.PAID);
        installmentRepository.save(inst);

        fee.setAmountPaid(fee.getAmountPaid().add(request.getAmount()));
        fee.setRemainingBalance(fee.getRemainingBalance().subtract(request.getAmount()));

        fee.setStatus(fee.getRemainingBalance().compareTo(BigDecimal.ZERO) <= 0
                ? FeeStatus.PAID : FeeStatus.PARTIALLY_PAID);

        studentFeeRepository.save(fee);

        PaymentTransaction tx = new PaymentTransaction();
        tx.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8));
        tx.setAmountPaid(request.getAmount());
        tx.setPaymentDate(LocalDateTime.now());
        tx.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        tx.setPaymentType(PaymentType.ONLINE);
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setStudentFee(fee);
        tx.setInstallment(inst);

        transactionRepository.save(tx);

        // EMAIL
        Student student = studentRepository.findById(request.getStudentId()).orElse(null);

        if (student != null && student.getParentEmail() != null) {
            String body = "<h3>Payment Success</h3>" +
                    "<p>Student: " + student.getName() + "</p>" +
                    "<p>Amount: ₹" + request.getAmount() + "</p>";

            try {
                emailService.sendHtmlEmail(student.getParentEmail(), "Payment Done", body);
            } catch (Exception ignored) {}
        }

        return tx;
    }

    // TRANSACTIONS

    public ResponseEntity<?> getStudentTransactions(Long studentId) {

        List<TransactionHistoryDTO> list =
                transactionRepository.findByStudentFee_StudentIdOrderByPaymentDateDesc(studentId)
                        .stream().map(tx -> {

                            TransactionHistoryDTO dto = new TransactionHistoryDTO();
                            dto.setTransactionId(tx.getTransactionId());
                            dto.setAmountPaid(tx.getAmountPaid());
                            dto.setPaymentDate(tx.getPaymentDate().toString());
                            dto.setPaymentMethod(tx.getPaymentMethod().toString());
                            dto.setStatus(tx.getStatus().toString());
                            dto.setInstallmentLabel(tx.getInstallment().getInstallmentName());

                            return dto;
                        }).toList();

        return ResponseEntity.ok(list);
    }
}