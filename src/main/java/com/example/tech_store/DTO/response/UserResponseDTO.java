package com.example.tech_store.DTO.response;

import com.example.tech_store.enums.Role;
import com.example.tech_store.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserResponseDTO {
    private String email;
    private String username;
    private String phoneNumber;
    private String avatar;
    private Role role;
    private String token;
    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    public static UserResponseDTO fromUser(User user) {
        return UserResponseDTO.builder()
                .email(user.getEmail())
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
