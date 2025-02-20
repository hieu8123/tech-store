package com.example.tech_store.DTO.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDTO {
    private UUID cartId;
    private List<CartItemRequestDTO> cartItems;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItemRequestDTO {
        private UUID productId;
        private int quantity;
    }
}
