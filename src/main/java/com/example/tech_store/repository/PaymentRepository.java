package com.example.tech_store.repository;

import com.example.tech_store.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Payment findByOrderId(UUID orderId);
    Payment findByTransactionId(String transactionId);
}
