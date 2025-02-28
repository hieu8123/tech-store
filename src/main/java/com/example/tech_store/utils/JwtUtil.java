package com.example.tech_store.utils;

import com.example.tech_store.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh_expiration}")
    private long refreshExpiration;

    public JwtUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 📌 Lấy khóa ký (HMAC SHA256)
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 📌 Sinh accessToken hoặc refreshToken
    public String generateToken(UUID userId, String email, boolean isRefreshToken) {
        long expirationTime = isRefreshToken ? refreshExpiration : jwtExpiration;
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("email", email)  // Thêm email vào claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 📌 Trích xuất userId từ token
    public UUID extractUserId(String token) {
        if (!isTokenValid(token))
            throw new UnauthorizedException("Invalid token");
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public String extractEmail(String token) {
        if (!isTokenValid(token)) {
            throw new UnauthorizedException("Invalid token");
        }
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    // 📌 Kiểm tra định dạng token hợp lệ không
    public boolean isTokenValid(String token) {
        if (token == null || token.split("\\.").length != 3) {
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 📌 Lấy các claim từ token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // 📌 Lưu token vào Redis (Token Storage)
    public void saveTokenToRedis(String token, UUID userId) {
        String tokenId = String.valueOf(extractClaim(token, Claims::getIssuedAt).getTime());
        String redisKey = userId.toString() + ":" + tokenId;
        redisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
    }

    // 📌 Kiểm tra token hợp lệ với userId
    public boolean validateToken(String token, UUID userId) {
        if (!isTokenValid(token)) {
            return false;
        }
        String tokenId = String.valueOf(extractClaim(token, Claims::getIssuedAt).getTime());
        String redisKey = userId.toString() + ":" + tokenId;
        String storedToken = (String) redisTemplate.opsForValue().get(redisKey);
        return storedToken != null && storedToken.equals(token) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    // 📌 Xóa token khỏi Redis khi logout
    public void removeTokenFromRedis(String token, UUID userId) {
        if (!isTokenValid(token)) {
            return;
        }
        String tokenId = String.valueOf(extractClaim(token, Claims::getIssuedAt).getTime());
        String redisKey = userId.toString() + ":" + tokenId;
        redisTemplate.delete(redisKey);
    }
}
