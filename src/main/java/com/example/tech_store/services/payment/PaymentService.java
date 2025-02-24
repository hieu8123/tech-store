package com.example.tech_store.services.payment;

import com.example.tech_store.enums.PaymentMethod;
import com.example.tech_store.model.Order;

import java.util.Map;


public interface PaymentService {
    PaymentMethod getPaymentMethod();
    String processPayment(Order order);
}
