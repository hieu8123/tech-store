package com.example.tech_store.repository;

import com.example.tech_store.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    public List<CartItem> findByCartId(UUID cartId);
    public Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);

}