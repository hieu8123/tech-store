package com.example.tech_store.DTO.request;

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
public class LoginRequestDTO {

    @NotBlank(message = "Email is blank")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is blank")
    private String password;

    public User toUser() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static LoginRequestDTO fromUser(User user) {
        return LoginRequestDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
