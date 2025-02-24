package com.example.tech_store.services.payment;

import com.example.tech_store.enums.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentFactory {
    private final Map<PaymentMethod, PaymentService> paymentServiceMap;

    public PaymentFactory(List<PaymentService> paymentServices) {
        this.paymentServiceMap = paymentServices.stream()
                .collect(Collectors.toMap(PaymentService::getPaymentMethod, service -> service));
    }

    public PaymentService getPaymentService(PaymentMethod method) {
        return paymentServiceMap.get(method);
    }
}