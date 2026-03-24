package com.sms.backend.Entities;

import com.sms.backend.Enum.FeeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "student_fee")
public class StudentFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private BigDecimal amountPaid;

    private BigDecimal remainingBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private FeeStatus status;

    @ManyToOne
    @JoinColumn(name = "fee_structure_id")
    private FeeStructure feeStructure;

    @OneToMany(mappedBy = "studentFee", cascade = CascadeType.ALL)
    private List<Installment> installments;

    @OneToMany(mappedBy = "studentFee", cascade = CascadeType.ALL)
    private List<PaymentTransaction> transactions;
}