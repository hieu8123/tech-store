package com.example.tech_store.kafka.consumer;

import com.example.tech_store.DTO.event.InventoryEvent;
import com.example.tech_store.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryConsumer {
    private final KafkaTemplate<String, InventoryEvent> kafkaTemplate;
    private final OrderService orderService;

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void checkInventory(InventoryEvent inventoryEvent) {
        inventoryEvent.setAvailable(orderService.checkProductStock(inventoryEvent.getOrderId()));
        kafkaTemplate.send("inventory-topic", inventoryEvent);
    }

}

