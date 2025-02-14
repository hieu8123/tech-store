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
public class LoginRequestDTO {
    @NotBlank(message = "email is blank")
    @Email(message = "email is not valid")
    private String email;

    private String password;

    public User toUser() {
        User user = new User();
        user.setUsername(this.email);
        user.setPassword(this.password);
        return user;
    }

    public static LoginRequestDTO fromUser(User user) {
        return new LoginRequestDTO(user.getUsername(), user.getPassword());
    }
}

