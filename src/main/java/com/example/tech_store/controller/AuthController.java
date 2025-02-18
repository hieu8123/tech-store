package com.example.tech_store.controller;

import com.example.tech_store.DTO.request.LoginRequestDTO;
import com.example.tech_store.DTO.request.RegisterRequestDTO;
import com.example.tech_store.DTO.response.ApiResponseDTO;
import com.example.tech_store.DTO.response.UserResponseDTO;
import com.example.tech_store.constants.ApiConstants;
import com.example.tech_store.exception.UnauthorizedException;
import com.example.tech_store.services.AuthService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(ApiConstants.Endpoints.AUTH)
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO registerInfo) {
        UserResponseDTO userResponseDTO = authService.register(registerInfo);
        ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.<UserResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .timestamp(new Date())
                .message("User registered successfully")
                .data(userResponseDTO)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginInfo) {
        UserResponseDTO userResponseDTO = authService.login(loginInfo);
        ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.<UserResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .timestamp(new Date())
                .message("Login successful")
                .data(userResponseDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/oauth2/login")
    public ResponseEntity<ApiResponseDTO<String>> redirectToGoogleLogin() {
        String loginUrl = "http://localhost:8080/oauth2/authorization/google";
        ApiResponseDTO<String> responseDTO = ApiResponseDTO.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .timestamp(new Date())
                .message("Redirect to Google login")
                .data(loginUrl)
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<Void>> logout(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid token");
        }
        String token = authHeader.substring(7);
        authService.logout(token);

        ApiResponseDTO<Void> response = ApiResponseDTO.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .timestamp(new Date())
                .message("Logout successful")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<ApiResponseDTO<String>> refreshToken(@RequestParam String refreshToken) {
        String newToken = authService.refreshToken(refreshToken);
        ApiResponseDTO<String> response = ApiResponseDTO.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .timestamp(new Date())
                .message("Token refreshed successfully")
                .data(newToken)
                .build();
        return ResponseEntity.ok(response);
    }
}
