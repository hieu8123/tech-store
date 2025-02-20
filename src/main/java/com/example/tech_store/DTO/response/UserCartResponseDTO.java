package com.example.tech_store.DTO.response;


import com.example.tech_store.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserCartResponseDTO {
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class CartItemResponseDTO {
        private UUID cartId;
        private UUID productId;
        private String productName;
        private int quantity;
        private int productPrice;
    }
    private UUID cartId;
    private List<CartItemResponseDTO> cartItems;

    public static UserCartResponseDTO fromCart(Cart cart) {
        return UserCartResponseDTO.builder()
                .cartId(cart.getId())
                .cartItems(
                        cart.getCartItems() != null ?
                                cart.getCartItems().stream().map(cartItem -> CartItemResponseDTO.builder()
                                        .cartId(cartItem.getId())
                                        .productId(cartItem.getProduct().getId())
                                        .productName(cartItem.getProduct().getName())
                                        .quantity(cartItem.getQuantity())
                                        .productPrice(cartItem.getProduct().getPrice())
                                        .build()).collect(Collectors.toList()) : null
                )
                .build();
    }

}
