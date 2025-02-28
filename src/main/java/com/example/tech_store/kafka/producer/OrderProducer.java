package com.example.tech_store.kafka.producer;

import com.example.tech_store.DTO.event.InventoryEvent;
import com.example.tech_store.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;
    private final PaymentRepository paymentRepository;

    public void sendInventoryCheckEvent(UUID orderId) {
        InventoryEvent event = new InventoryEvent(orderId, true);
        kafkaTemplate.send("inventory-topic", event);
    }
}
