package com.sms.backend.Entities;

import com.sms.backend.Enum.PaymentMethod;
import com.sms.backend.Enum.PaymentType;
import com.sms.backend.Enum.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_transaction")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String transactionId;

    private BigDecimal amountPaid;

    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "student_fee_id")
    private StudentFee studentFee;

    @ManyToOne
    @JoinColumn(name = "installment_id")
    private Installment installment;
}