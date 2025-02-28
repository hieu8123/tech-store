package com.example.tech_store.DTO.request;

import com.example.tech_store.enums.OrderStatus;
import com.example.tech_store.enums.PaymentMethod;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    @Data
    @AllArgsConstructor
    @Builder
    public static class OrderDetailsDTO {
        UUID productId;
        String productName;
        int quantity;
        int price;
    }
    private UUID userId;
    private PaymentMethod paymentMethod;
    private int total;
    private String note;
    private List<OrderDetailsDTO> orderDetails;

}
