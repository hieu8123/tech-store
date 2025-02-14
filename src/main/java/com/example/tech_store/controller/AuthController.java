package com.example.tech_store.controller;

import com.example.tech_store.DTO.request.LoginRequestDTO;
import com.example.tech_store.DTO.request.RegisterRequestDTO;
import com.example.tech_store.DTO.response.UserResponseDTO;
import com.example.tech_store.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerInfo) {
            UserResponseDTO userResponseDTO = authService.register(registerInfo);
            return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginInfo) {
            UserResponseDTO userResponseDTO = authService.login(loginInfo);
            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }

    @GetMapping("/oauth2/login")
    public String redirectToGoogleLogin(HttpServletResponse response) throws IOException {
         return "http://localhost:8080/oauth2/authorization/google";
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestParam String refreshToken) {
            String newToken = authService.refreshToken(refreshToken);
        return new ResponseEntity<>(newToken, HttpStatus.OK);
    }
}
