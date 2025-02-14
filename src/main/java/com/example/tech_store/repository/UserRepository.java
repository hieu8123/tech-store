package com.example.tech_store.repository;

import com.example.tech_store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String Email);
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
}