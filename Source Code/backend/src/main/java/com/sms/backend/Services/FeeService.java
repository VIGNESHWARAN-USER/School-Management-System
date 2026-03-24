package com.sms.backend.Services;

import com.sms.backend.DTO.*;
import com.sms.backend.Entities.*;
import com.sms.backend.Enum.*;
import com.sms.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FeeService {

    @Autowired
    private FeeStructureRepository feeStructureRepository;
    @Autowired
    private FeeComponentRepository feeComponentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentFeeRepository studentFeeRepository;
    @Autowired
    private InstallmentRepository installmentRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    public ResponseEntity<?> createFeeStructure(FeeStructureDTO dto) {
        try{

            FeeStructure structure = feeStructureRepository.findByClassIdAndAcademicYear(dto.getClassId(), dto.getAcademicYear());
            if(structure != null)
            {
                List<FeeComponent> components = new ArrayList<>();
                double grandTotal = 0.0;

                for (FeeComponentDTO cDto : dto.getComponents()) {
                    FeeComponent component = new FeeComponent();
                    component.setComponentName(FeeComponentType.valueOf(cDto.getComponentName()));
                    component.setDescription(cDto.getDescription());
                    component.setAmount(cDto.getAmount());
                    component.setTaxPercentage(cDto.getTaxPercentage());


                    double taxAmount = (cDto.getAmount() * cDto.getTaxPercentage()) / 100;
                    double totalForComponent = cDto.getAmount() + taxAmount;


                    component.setFeeStructure(structure);
                    components.add(component);
                    grandTotal += totalForComponent;
                }

                structure.setComponents(components);
                structure.setTotalAmount(BigDecimal.valueOf(grandTotal));
                feeComponentRepository.saveAll(components);
                feeStructureRepository.save(structure);
            }
            else {
                structure = new FeeStructure();
                structure.setClassId(dto.getClassId());
                structure.setAcademicYear(dto.getAcademicYear());

                List<FeeComponent> components = new ArrayList<>();
                double grandTotal = 0.0;

                for (FeeComponentDTO cDto : dto.getComponents()) {
                    FeeComponent component = new FeeComponent();
                    component.setComponentName(FeeComponentType.valueOf(cDto.getComponentName()));
                    component.setDescription(cDto.getDescription());
                    component.setAmount(cDto.getAmount());
                    component.setTaxPercentage(cDto.getTaxPercentage());


                    double taxAmount = (cDto.getAmount() * cDto.getTaxPercentage()) / 100;
                    double totalForComponent = cDto.getAmount() + taxAmount;


                    component.setFeeStructure(structure);
                    components.add(component);
                    grandTotal += totalForComponent;
                }

                structure.setComponents(components);
                structure.setTotalAmount(BigDecimal.valueOf(grandTotal));
                feeComponentRepository.saveAll(components);
                feeStructureRepository.save(structure);
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(structure);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    public ResponseEntity<?> getStructure(String classId, String academicYear) {
        FeeStructure feeStructure = feeStructureRepository.findByClassIdAndAcademicYear(classId, academicYear);

        FeeStructureDTO feeStructureDTO = new FeeStructureDTO();

        feeStructureDTO.setAcademicYear(feeStructure.getAcademicYear());
        feeStructureDTO.setClassId(feeStructure.getClassId());

        List<FeeComponentDTO> feeComponentDTOS = feeStructure.getComponents().stream().map(dto ->{
            FeeComponentDTO feeComponentDTO = new FeeComponentDTO();

            feeComponentDTO.setComponentName(String.valueOf(dto.getComponentName()));
            feeComponentDTO.setAmount(dto.getAmount());
            feeComponentDTO.setDescription(dto.getDescription());
            feeComponentDTO.setTaxPercentage(dto.getTaxPercentage());

            return feeComponentDTO;
        }).toList();

        feeStructureDTO.setComponents(feeComponentDTOS);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(feeStructureDTO);
    }

    public ResponseEntity<?> getAllStructure() {
        List<FeeStructure> feeStructures = feeStructureRepository.findAll();
        List<FeeStructureDTO> feeStructureDTOS = feeStructures.stream().map(feeStructure ->
        {
            FeeStructureDTO feeStructureDTO = new FeeStructureDTO();
            feeStructureDTO.setClassId(feeStructure.getClassId());
            feeStructureDTO.setAcademicYear(feeStructure.getAcademicYear());
            feeStructureDTO.setFeeStructureId(feeStructure.getId());
            feeStructureDTO.setTotalAmount(feeStructure.getTotalAmount());

            return feeStructureDTO;
        }).toList();
        return ResponseEntity.status(200).body(feeStructureDTOS);
    }

    @Transactional
    public void saveAndApplyInstallments(InstallmentPlanDTO dto) {
        FeeStructure structure = feeStructureRepository.findById(dto.getFeeStructureId())
                .orElseThrow(() -> new RuntimeException("Structure not found"));


        List<Student> students = studentRepository.findAllByClassId(structure.getClassId());

        for (Student student : students) {

            StudentFee studentFee = studentFeeRepository.findByStudentId(student.getId());

            if(studentFee == null) studentFee = new StudentFee();

            studentFee.setStudentId(student.getId());
            studentFee.setFeeStructure(structure);
            studentFee.setAmountPaid(BigDecimal.ZERO);
            studentFee.setRemainingBalance(structure.getTotalAmount());
            studentFee.setStatus(FeeStatus.PENDING);

            final StudentFee savedFee = studentFeeRepository.save(studentFee);
            installmentRepository.deleteByStudentFee(savedFee);
            for (InstallmentItemDTO instDto : dto.getInstallments()) {
                Installment installment = new Installment();
                installment.setDueDate(LocalDate.parse(instDto.getDueDate()));
                installment.setStatus(PaymentStatus.PENDING);
                installment.setInstallmentName(instDto.getInstallmentName());
                installment.setStudentFee(savedFee);

                // Calculate BigDecimal amount: (Total * Percentage) / 100
                BigDecimal instAmount = structure.getTotalAmount()
                        .multiply(BigDecimal.valueOf(instDto.getPercentage()))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                installment.setAmount(instAmount);
                installmentRepository.save(installment);
            }
        }
    }

    public ResponseEntity<?> getInstallments(Long id) {
        StudentFee studentFee = studentFeeRepository.findAllByFeeStructure(feeStructureRepository.findById(id).orElse(null)).getFirst();

        List<Installment> installments = studentFee.getInstallments();
        List<InstallmentItemDTO> installmentItemDTOS = installments.stream().map(inst -> {
            InstallmentItemDTO dto = new InstallmentItemDTO();
            dto.setInstallmentName(inst.getInstallmentName());
            dto.setDueDate(inst.getDueDate().toString());


            double percentage = inst.getAmount()
                    .divide(studentFee.getFeeStructure().getTotalAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();

            dto.setPercentage(percentage);
            return dto;
        }).toList();
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(installmentItemDTOS);
    }

    public FeeDashboardDTO getStudentFeeDashboard(Long studentId) {
        // 1. Fetch Entity
        StudentFee fee = studentFeeRepository.findByStudentId(studentId);
        if(fee == null) throw new RuntimeException("Fee records not found for student: " + studentId);

        // 2. Map to DTO
        FeeDashboardDTO dto = new FeeDashboardDTO();
        dto.setStudentId(fee.getStudentId());
        dto.setAmountPaid(fee.getAmountPaid());
        dto.setRemainingBalance(fee.getRemainingBalance());
        dto.setOverallStatus(fee.getStatus().toString());

        // Map Structure Info
        if (fee.getFeeStructure() != null) {
            dto.setClassId(fee.getFeeStructure().getClassId());
            dto.setAcademicYear(fee.getFeeStructure().getAcademicYear());
            dto.setTotalFeeWithTax(fee.getFeeStructure().getTotalAmount());

            // Map Components (US_002 Breakdown)
            dto.setBreakdown(fee.getFeeStructure().getComponents().stream().map(c -> {
                ComponentDTO cDto = new ComponentDTO();
                cDto.setName(c.getComponentName().toString().replace("_", " "));
                cDto.setDescription(c.getDescription());
                cDto.setAmount(c.getAmount());
                cDto.setTaxPercentage(c.getTaxPercentage());
                return cDto;
            }).toList());
        }

        // Map Installments (US_012 Schedule)
        dto.setInstallments(fee.getInstallments().stream().map(i -> {
            InstallmentDTO iDto = new InstallmentDTO();
            iDto.setId(i.getId());
            iDto.setName(i.getInstallmentName());
            iDto.setAmount(i.getAmount());
            iDto.setDueDate(i.getDueDate().toString());
            iDto.setStatus(i.getStatus().toString());
            return iDto;
        }).toList());

        return dto;
    }


    @Transactional
    public PaymentTransaction processPayment(PaymentRequestDTO request) {

        StudentFee fee = studentFeeRepository.findByStudentId(request.getStudentId());
        if(fee == null) throw new RuntimeException("Fee record not found");

        Installment installment = installmentRepository.findById(request.getInstallmentId())
                .orElseThrow(() -> new RuntimeException("Installment not found"));

        if (installment.getStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("This installment is already paid.");
        }

        installment.setStatus(PaymentStatus.PAID);
        installmentRepository.save(installment);

        fee.setAmountPaid(fee.getAmountPaid().add(request.getAmount()));
        fee.setRemainingBalance(fee.getRemainingBalance().subtract(request.getAmount()));

        if (fee.getRemainingBalance().compareTo(BigDecimal.ZERO) <= 0) {
            fee.setStatus(FeeStatus.PAID);
        } else {
            fee.setStatus(FeeStatus.PARTIALLY_PAID);
        }
        studentFeeRepository.save(fee);

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        transaction.setAmountPaid(request.getAmount());
        transaction.setPaymentDate(LocalDateTime.now());
        transaction.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase().replace(" ", "_")));
        transaction.setPaymentType(PaymentType.ONLINE);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setStudentFee(fee);
        transaction.setInstallment(installment);
        transaction.setRemarks("Paid online for installment #" + installment.getInstallmentNumber());

        return transactionRepository.save(transaction);
    }

    public Object getStudentTransactions(Long studentId) {
        List<PaymentTransaction> txs = transactionRepository.findByStudentFee_StudentIdOrderByPaymentDateDesc(studentId);

        List<TransactionHistoryDTO> dtos = txs.stream().map(tx -> {
            TransactionHistoryDTO dto = new TransactionHistoryDTO();
            dto.setTransactionId(tx.getTransactionId());
            dto.setAmountPaid(tx.getAmountPaid());
            dto.setPaymentDate(tx.getPaymentDate().toString());
            dto.setPaymentMethod(tx.getPaymentMethod().toString());
            dto.setPaymentType(tx.getPaymentType().toString());
            dto.setStatus(tx.getStatus().toString());
            dto.setInstallmentLabel(tx.getInstallment().getInstallmentName());
            dto.setRemarks(tx.getRemarks());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
}
}