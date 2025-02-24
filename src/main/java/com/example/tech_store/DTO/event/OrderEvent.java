package com.example.tech_store.DTO.event;

import com.example.tech_store.enums.PaymentMethod;
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
public class OrderEvent {
    public static class OrderDetails{
        UUID productId;
        String productName;
        int quantity;
        int price;
    }
    private UUID orderId;
    private UUID userId;
    private Integer total;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private OrderDetails orderDetails;

    @JsonCreator
    public OrderEvent(
            @JsonProperty("orderId") String orderId,
            @JsonProperty("userId") String userId,
            @JsonProperty("total") Integer total,
            @JsonProperty("paymentMethod") PaymentMethod paymentMethod,
            @JsonProperty("status") PaymentStatus status) {
        this.orderId = UUID.fromString(orderId);
        this.userId = UUID.fromString(userId);
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }
}