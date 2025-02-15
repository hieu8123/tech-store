package com.example.tech_store.config;

import com.example.tech_store.model.User;
import com.example.tech_store.repository.UserRepository;
import com.example.tech_store.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository; // Service để load user

    public JwtAuthenticationProvider(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        // Trích xuất userId từ token
        UUID userId = jwtUtil.extractUserId(token);

        User user =  userRepository.findById(userId).orElse(null);
        // Trả về đối tượng Authentication đã được xác thực chứa thông tin user và quyền hạn của user


        assert user != null;
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
