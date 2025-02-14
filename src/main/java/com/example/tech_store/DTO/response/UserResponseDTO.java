package com.example.tech_store.DTO.response;

import com.example.tech_store.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String username;
    private String token;
    private String refreshToken;

    // Chuyển đổi từ RegisterDTO sang User entity
    public User toUser() {
        User user = new User();
        user.setUsername(this.username);// Mật khẩu cần mã hóa trước khi lưu
        return user;
    }

    // Chuyển đổi từ User entity sang RegisterDTO
    public static UserResponseDTO fromUser(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        return userResponseDTO;
    }
}
