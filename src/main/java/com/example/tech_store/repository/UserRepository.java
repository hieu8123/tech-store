package com.example.tech_store.repository;

import com.example.tech_store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String Email);
    @Query("SELECT u.email FROM User u")
    List<String> findAllEmails();
}