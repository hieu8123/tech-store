package com.example.tech_store.repository;

import com.example.tech_store.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    public List<OrderDetail> findByOrderId(UUID orderId);
}