package com.example.tech_store.kafka.producer;

import com.example.tech_store.DTO.event.InventoryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    public void sendInventoryCheckEvent(UUID orderId) {
        InventoryEvent event = new InventoryEvent(orderId, true);
        kafkaTemplate.send("order-topic", event);
    }
}
