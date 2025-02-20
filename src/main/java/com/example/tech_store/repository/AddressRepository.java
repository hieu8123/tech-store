package com.example.tech_store.repository;

import com.example.tech_store.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Page<Address> findByUserId(UUID userId, Pageable pageable);
}