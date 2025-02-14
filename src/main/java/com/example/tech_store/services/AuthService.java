package com.example.tech_store.services;

import com.example.tech_store.DTO.request.LoginRequestDTO;
import com.example.tech_store.DTO.request.RegisterRequestDTO;
import com.example.tech_store.DTO.response.UserResponseDTO;
import com.example.tech_store.model.User;
import com.example.tech_store.model.RefreshToken;
import com.example.tech_store.repository.UserRepository;
import com.example.tech_store.repository.RefreshTokenRepository;
import com.example.tech_store.utils.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    // 📌 Đăng ký tài khoản
    public UserResponseDTO register(RegisterRequestDTO registerInfo) {
        try {
            registerInfo.setPassword(passwordEncoder.encode(registerInfo.getPassword()));
            User user = registerInfo.toUser();
            userRepository.save(user);
            // Tạo và lưu token
            return generateAndSaveTokens(user);
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage());
        }
    }


    // 📌 Đăng nhập -> Trả về accessToken & refreshToken
    public UserResponseDTO login(LoginRequestDTO loginInfo) {
        Optional<User> optionalUser = userRepository.findByEmail(loginInfo.getEmail());

        // Kiểm tra xem người dùng có tồn tại không
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        User user = optionalUser.get(); // Lấy User từ Optional
        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }
        // Tạo và lưu token
        return generateAndSaveTokens(user);
    }

    public UserResponseDTO oauth2Login(String email) {
        // Kiểm tra user trong DB
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setRole("USER");
                    return userRepository.save(newUser);
                });

        return generateAndSaveTokens(user);
    }

    // 📌 Làm mới Access Token
    public String refreshToken(String refreshToken) {
        Optional<RefreshToken> storedToken = refreshTokenRepository.findByToken(refreshToken);
        if (storedToken.isEmpty() || jwtUtil.isTokenExpired(storedToken.get().getToken())) {
            throw new RuntimeException("Invalid or expired refresh token!");
        }
        UUID userId = jwtUtil.extractUserId(storedToken.get().getToken());
        return jwtUtil.generateToken(userId, false);
    }

    // 📌 Đăng xuất -> Xóa Refresh Token khỏi DB
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    // Phương thức dùng chung để tạo và lưu token
    private UserResponseDTO generateAndSaveTokens(User user) {
        // Tạo token
        String accessToken = jwtUtil.generateToken(user.getId(), false);
        String refreshToken = jwtUtil.generateToken(user.getId(), true);

        // Lưu token vào DB và Redis
        saveToken(user, accessToken, refreshToken);

        // Chuyển đổi thành UserResponseDTO
        UserResponseDTO userResponseDTO = UserResponseDTO.fromUser(user);
        userResponseDTO.setToken(accessToken);
        userResponseDTO.setRefreshToken(refreshToken);

        return userResponseDTO;
    }

    // Phương thức lưu token vào DB và Redis
    private void saveToken(User user, String accessToken, String refreshToken) {
        RefreshToken refresh = new RefreshToken();
        refresh.setToken(refreshToken);
        refresh.setUser(user); // Lưu user trực tiếp thay vì optional
        refreshTokenRepository.save(refresh);
        // Lưu token vào Redis
        jwtUtil.saveTokenToRedis(accessToken, user.getId());
    }
}

