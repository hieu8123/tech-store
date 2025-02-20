package com.example.tech_store.repository;

import com.example.tech_store.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
