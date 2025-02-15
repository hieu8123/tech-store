package com.example.tech_store.DTO.response;

import com.example.tech_store.enums.Role;
import com.example.tech_store.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserResponseDTO {
    private String username;
    private String phoneNumber;
    private String avatar;
    private Role role;
    private String token;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponseDTO fromUser(User user) {
        return UserResponseDTO.builder()
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .token(null)
                .refreshToken(user.getRefreshToken() != null ? user.getRefreshToken().getToken() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
