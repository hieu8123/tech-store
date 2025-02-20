package com.example.tech_store.DTO.event;

import com.example.tech_store.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEvent {
    private UUID orderId;
    private PaymentStatus paymentStatus;

    @JsonCreator
    public PaymentEvent(
            @JsonProperty("orderId") String orderId,
            @JsonProperty("paymentStatus") PaymentStatus paymentStatus) {
        this.orderId = UUID.fromString(orderId);
        this.paymentStatus = paymentStatus;
    }
}

