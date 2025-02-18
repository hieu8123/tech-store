package com.example.tech_store.controller;

import com.example.tech_store.DTO.response.ApiResponseDTO;
import com.example.tech_store.DTO.response.UserProfileResponseDTO;
import com.example.tech_store.constants.ApiConstants;
import com.example.tech_store.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.Endpoints.USERS)
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> getUser(@PathVariable UUID id) {
        Optional<UserProfileResponseDTO>user = userService.getUserProfile(id);
        ApiResponseDTO<UserProfileResponseDTO> response = user.map(u -> ApiResponseDTO.<UserProfileResponseDTO>builder()
                        .timestamp(new Date())
                        .success(true)
                        .status(HttpStatus.OK.value())
                        .message("Get user profile successfully")
                        .data(u)
                        .build())
                .orElse(ApiResponseDTO.<UserProfileResponseDTO>builder()
                        .timestamp(new Date())
                        .success(false)
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("user not found")
                        .build());
        return user.isPresent() ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }
}
