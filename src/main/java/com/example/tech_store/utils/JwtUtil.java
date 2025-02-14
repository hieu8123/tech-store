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

    final private RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh_expiration}")
    private long refreshExpiration;

    public JwtUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    // Phương thức lấy khóa ký, đảm bảo secretKey phải có đủ độ dài (ít nhất 256-bit đối với HS256)
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Sinh token, nếu isRefreshToken = true thì sử dụng thời gian refresh token
    public String generateToken(UUID userId, boolean isRefreshToken) {
        long expirationTime = isRefreshToken ? refreshExpiration : jwtExpiration;
        return Jwts.builder()
                .setSubject(userId.toString()) // Lưu trữ userId dưới dạng String
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Lấy userId từ token
    public UUID extractUserId(String token) {
        if (!isTokenValidFormat(token))
            throw new UnauthorizedException("Invalid token format");
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public boolean isTokenValidFormat(String token) {
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

    // Lấy các claim theo hàm truyền vào
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public void saveTokenToRedis(String token, UUID userId) {
        String tokenId = String.valueOf(extractClaim(token, Claims::getIssuedAt).getTime());
        String redisKey = userId.toString() + ":" + tokenId;
        redisTemplate.opsForValue().set(redisKey, token, jwtExpiration, TimeUnit.MILLISECONDS);
    }

    // Xác thực token với userId
    public boolean validateToken(String token, UUID userId) {
        String tokenId = String.valueOf(extractClaim(token, Claims::getIssuedAt).getTime());
        String redisKey = userId.toString() + ":" + tokenId;
        String storedToken = (String) redisTemplate.opsForValue().get(redisKey);
        return storedToken != null && storedToken.equals(token) && !isTokenExpired(token);
    }


    // Kiểm tra token đã hết hạn chưa
    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }



}
