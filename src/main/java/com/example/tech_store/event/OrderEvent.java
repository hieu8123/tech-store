package com.example.tech_store.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class OrderEvent {
    private UUID orderId;
    private UUID userId;
    private Integer total;
    private String status;
}