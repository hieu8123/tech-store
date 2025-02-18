package com.example.tech_store.services;

import com.example.tech_store.DTO.response.UserProfileResponseDTO;
import com.example.tech_store.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Optional<UserProfileResponseDTO>  getUserProfile(UUID userId) {
        return  userRepository.findById(userId).map(UserProfileResponseDTO::fromUser);
    }

}
