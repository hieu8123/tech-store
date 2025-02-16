package com.example.tech_store.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic orderTopic() {
        return new NewTopic("order-events", 3, (short) 1);
    }

    @Bean
    public NewTopic inventoryTopic() {
        return new NewTopic("inventory-events", 3, (short) 1);
    }

    @Bean
    public NewTopic paymentTopic() {
        return new NewTopic("payment-events", 3, (short) 1);
    }
}