package com.example.tech_store.kafka.consumer;

import com.example.tech_store.DTO.event.InventoryEvent;
import com.example.tech_store.DTO.event.PaymentEvent;
import com.example.tech_store.enums.PaymentStatus;
import com.example.tech_store.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private final OrderService orderService;


    @KafkaListener(topics = "payment-topic", groupId = "payment-group")
    public void processPayment(InventoryEvent inventoryEvent) {
        if (!inventoryEvent.isAvailable()) {
            orderService.cancelOrder(inventoryEvent.getOrderId());
            return;
        }
        PaymentStatus paymentStatus = PaymentStatus.PENDING;
        PaymentEvent paymentEvent = new PaymentEvent(inventoryEvent.getOrderId(), paymentStatus);
    }
}

