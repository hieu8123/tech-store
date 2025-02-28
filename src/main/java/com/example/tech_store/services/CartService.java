package com.example.tech_store.services;

import com.example.tech_store.DTO.request.CartRequestDTO;
import com.example.tech_store.DTO.response.UserCartResponseDTO;
import com.example.tech_store.model.Cart;
import com.example.tech_store.model.CartItem;
import com.example.tech_store.repository.CartItemRepository;
import com.example.tech_store.repository.CartRepository;
import com.example.tech_store.repository.ProductRepository;
import com.example.tech_store.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    private final ProductRepository productRepository;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    public Optional<UserCartResponseDTO> getUserCart(UUID userId) {
        return cartRepository.findByUserId(userId)
                .map(UserCartResponseDTO::fromCart)
                .or(() -> Optional.of(createNewCartForUser(userId)));
    }


    private UserCartResponseDTO createNewCartForUser(UUID userId) {
        Cart newCart = Cart.builder()
                .user(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")))
                .build();

        cartRepository.save(newCart);
        return UserCartResponseDTO.fromCart(newCart);
    }
    @Transactional
    public Optional<UserCartResponseDTO> updateUserCart(UUID userId, CartRequestDTO cartRequest) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
            return cartRepository.save(newCart);
        });

        cartItemRepository.deleteAll(cart.getCartItems());

        List<CartItem> newCartItems = cartRequest.getCartItems().stream().map(item -> {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found")));
            cartItem.setQuantity(item.getQuantity());
            return cartItem;
        }).collect(Collectors.toList());

        cart.setCartItems(newCartItems);
        cartRepository.save(cart);
        cartItemRepository.saveAll(newCartItems);

        return getUserCart(userId);
    }

    @Transactional
    public void clearUserCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
            return cartRepository.save(newCart);
        });

        cartItemRepository.deleteAll(cart.getCartItems());
    }
}
