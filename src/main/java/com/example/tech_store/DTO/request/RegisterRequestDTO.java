package com.example.tech_store.DTO.request;

import com.example.tech_store.enums.Role;
import com.example.tech_store.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Email is blank")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Username is blank")
    private String username;

    @NotBlank(message = "Password is blank")
    private String password;

    private Role role;

    public User toUser() {
        return User.builder()
                .email(email)
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

    public static RegisterRequestDTO fromUser(User user) {
        return RegisterRequestDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}
