package com.example.tech_store.services;

import com.example.tech_store.DTO.request.LoginRequestDTO;
import com.example.tech_store.DTO.request.RegisterRequestDTO;
import com.example.tech_store.DTO.response.UserResponseDTO;
import com.example.tech_store.enums.Role;
import com.example.tech_store.exception.InvalidDataException;
import com.example.tech_store.model.RefreshToken;
import com.example.tech_store.model.User;
import com.example.tech_store.repository.RefreshTokenRepository;
import com.example.tech_store.repository.UserRepository;
import com.example.tech_store.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final BloomFilterService bloomFilterService;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       JwtUtil jwtUtil,
                       BloomFilterService bloomFilterService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.bloomFilterService = bloomFilterService;
    }

    public UserResponseDTO register(RegisterRequestDTO registerInfo) {
        try {
            registerInfo.setPassword(passwordEncoder.encode(registerInfo.getPassword()));
            if(!bloomFilterService.mightContain(registerInfo.getEmail())) {
                User user = registerInfo.toUser();
                userRepository.save(user);
                return generateAndSaveTokens(user);
            }
            throw new InvalidDataException("Error registering user:Email already exists");
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }

    public UserResponseDTO login(LoginRequestDTO loginInfo) {

        if(!bloomFilterService.mightContain(loginInfo.getEmail())) {
            throw new InvalidDataException("Email does not exist");
        }
        Optional<User> optionalUser = userRepository.findByEmail(loginInfo.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }
        return generateAndSaveTokens(user);
    }

    public UserResponseDTO oauth2Login(String email) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setRole(Role.USER);
                    return userRepository.save(newUser);
                });

        return generateAndSaveTokens(user);
    }

    public String refreshToken(String refreshToken) {
        Optional<RefreshToken> storedToken = refreshTokenRepository.findByToken(refreshToken);
        if (storedToken.isEmpty() || jwtUtil.isTokenExpired(storedToken.get().getToken())) {
            throw new RuntimeException("Invalid or expired refresh token!");
        }
        UUID userId = jwtUtil.extractUserId(storedToken.get().getToken());
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        String accessToken =  jwtUtil.generateToken(user.getId(),user.getEmail(), false);
        jwtUtil.saveTokenToRedis(accessToken, user.getId());
        return accessToken;
    }

    public void logout(String accessToken) {
        UUID userId = jwtUtil.extractUserId(accessToken);
        User user = userRepository.findById(userId).orElse(null);
        if(user != null) {
            if(user.getRefreshToken() != null) {
                user.setRefreshToken(null);
            }
            jwtUtil.blacklistToken(accessToken);
        }

    }

    private UserResponseDTO generateAndSaveTokens(User user) {
        String accessToken = jwtUtil.generateToken(user.getId(),user.getEmail(), false);

        String refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .map(RefreshToken::getToken)
                .orElseGet(() -> {
                    String newRefreshToken = jwtUtil.generateToken(user.getId(),user.getEmail(),true);
                    RefreshToken newToken = RefreshToken.builder()
                            .user(user)
                            .token(newRefreshToken)
                            .build();
                    refreshTokenRepository.save(newToken);
                    return newRefreshToken;
                });

        jwtUtil.saveTokenToRedis(accessToken, user.getId());

        UserResponseDTO userResponseDTO= UserResponseDTO.fromUser(user);
        userResponseDTO.setToken(accessToken);
        userResponseDTO.setRefreshToken(refreshToken);
        return userResponseDTO;
    }

}

