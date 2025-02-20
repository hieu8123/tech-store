package com.example.tech_store.DTO.response;

import com.example.tech_store.enums.Role;
import com.example.tech_store.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponseDTO {
    private UUID userId;
    private String email;
    private String username;
    private String phoneNumber;
    private String avatar;
    private Role role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    public static UserProfileResponseDTO fromUser(User user) {
        return UserProfileResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

