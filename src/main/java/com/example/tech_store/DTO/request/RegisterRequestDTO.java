package com.example.tech_store.DTO.request;

import com.example.tech_store.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "email is blank")
    @Email(message = "email is not valid")
    private String email;

    @NotBlank(message = "username is blank")
    private String username;

    @NotBlank(message = "password is blank")
    private String password;
    private String role;

    public User toUser() {
        User user = new User();
        user.setEmail(email);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setRole(this.role);
        return user;
    }

    // Chuyển đổi từ User entity sang RegisterDTO
    public static RegisterRequestDTO fromUser(User user) {
        return new RegisterRequestDTO(user.getEmail(),user.getUsername(), user.getPassword(), user.getRole());
    }
}
