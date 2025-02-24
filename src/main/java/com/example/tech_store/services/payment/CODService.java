package com.example.tech_store.services.payment;

import com.example.tech_store.enums.PaymentMethod;
import com.example.tech_store.enums.PaymentStatus;
import com.example.tech_store.model.Order;
import com.example.tech_store.model.Payment;
import com.example.tech_store.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CODService implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CASH_ON_DELIVERY;
    }

    @Override
    public String processPayment(Order order) {
        Payment payment = paymentRepository.findByOrderId(order.getId());
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
        return "COD payment recorded";
    }

}