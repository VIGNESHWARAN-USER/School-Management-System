package com.sms.backend.Repositories;

import com.sms.backend.Entities.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentTransaction, Long> {

}