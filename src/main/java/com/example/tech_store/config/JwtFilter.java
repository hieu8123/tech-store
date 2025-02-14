package com.example.tech_store.config;

import com.example.tech_store.DTO.response.ErrorResponse;
import com.example.tech_store.exception.UnauthorizedException;
import com.example.tech_store.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public JwtFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain chain)
            throws ServletException, IOException {
        try {
            String token = extractAndValidateToken(request);
            if (token != null) {
                authenticateToken(token);
            }
            chain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(new Date());
            errorResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.setError("Unauthorized");
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setPath(request.getRequestURI());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
            return;

        }


    }

    private String extractAndValidateToken(HttpServletRequest request) throws UnauthorizedException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValidFormat(token)) {
            System.out.println("Token is not valid");
            throw new UnauthorizedException("Token format is invalid");
        }
        UUID userId = jwtUtil.extractUserId(token);
        if (!jwtUtil.validateToken(token, userId)) {
            throw new UnauthorizedException("Invalid or expired token") {
            };
        }

        return token;
    }

    private void authenticateToken(String token) throws UnauthorizedException {
        UUID userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            throw new UnauthorizedException("User ID not found in token");
        }
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(null, token);
            var authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException ex) {
            System.out.println(ex.getMessage());
            throw new UnauthorizedException("Authentication failed: " + ex.getMessage());
        }
    }
}