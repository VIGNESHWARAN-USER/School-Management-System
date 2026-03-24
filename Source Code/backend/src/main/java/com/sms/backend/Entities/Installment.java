package com.sms.backend.Entities;


import com.sms.backend.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "installment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer installmentNumber;

    private String installmentName;

    private BigDecimal amount;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "student_fee_id")
    private StudentFee studentFee;

    @Override
    public String toString() {
        return "Installment{" +
                "id=" + id +
                ", installmentNumber=" + installmentNumber +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", studentFee=" + studentFee +
                '}';
    }
}
