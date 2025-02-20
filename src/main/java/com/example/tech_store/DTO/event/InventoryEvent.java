package com.example.tech_store.DTO.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEvent {
    private UUID orderId;
    private boolean isAvailable;

    @JsonCreator
    public InventoryEvent(
            @JsonProperty("orderId") String orderId,
            @JsonProperty("isAvailable") boolean isAvailable) {
        this.orderId = UUID.fromString(orderId);
        this.isAvailable = isAvailable;
    }
}
