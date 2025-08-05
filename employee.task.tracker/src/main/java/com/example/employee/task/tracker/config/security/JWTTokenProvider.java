package com.example.employee.task.tracker.config.security;


import com.example.employee.task.tracker.model.AuthProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private String accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private String refreshTokenExpiration;

    private final String CUR_DEPARTMENT_CLAIM = "departmentCode";

    public JWTTokenProvider(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public String createAccessToken(String username, String role, String departmentCode) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put(CUR_DEPARTMENT_CLAIM, departmentCode);
        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.parseLong(accessTokenExpiration));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createAccessToken(String username, String role, AuthProvider authProvider) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        String PROVIDER_CLAIM = "provider";
        claims.put(PROVIDER_CLAIM, authProvider.getRegistrationId());
        Date now = new Date();
        Date exp = new Date(now.getTime() +Long.parseLong( accessTokenExpiration));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
    public String createRefreshToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("type", "refresh");
        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.parseLong(refreshTokenExpiration));
        String refreshToken= Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        String key = "jwt_refresh_token:" + username;
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("refresh_token", refreshToken);
        tokenMap.put("expires_at", String.valueOf(exp.getTime()));
        redisTemplate.opsForHash().putAll(key, tokenMap);
        Duration ttl = Duration.between(Instant.now(), exp.toInstant());
        redisTemplate.expire(key, ttl);
        return refreshToken;
    }
    public String getRefreshToken(String username) {
        String key="jwt_refresh_token:" + username;
        if(!Boolean.TRUE.equals(redisTemplate.hasKey(key)))
            return null;
        Map<Object, Object> tokenData = redisTemplate.opsForHash().entries(key);
        String refreshToken = (String) tokenData.get("refresh_token");
        String expiresAtStr = (String) tokenData.get("expires_at");
        Instant expiresAt = Instant.ofEpochMilli(Long.parseLong(expiresAtStr));
        if(!expiresAt.isAfter(Instant.now()))
            return null;
        return refreshToken;
    }
    public boolean isRefreshToken(String token) {
        Claims claims = getClaims(token);
        return "refresh".equals(claims.get("type"));
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token,UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return bearer != null && bearer.startsWith("Bearer ") ? bearer.substring(7) : null;
    }

    public boolean validateToken(String token) {
        try {

            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;

        }
    }

    public Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return  Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
    }

    public String getCurrentDepartment(HttpServletRequest request) {
        String token = this.resolveAccessToken(request);
        if (token != null) {
            Claims claims = this.getClaims(token);
            if (claims.get(CUR_DEPARTMENT_CLAIM) != null && StringUtils.hasText(claims.get(CUR_DEPARTMENT_CLAIM).toString())) {
                return claims.get(CUR_DEPARTMENT_CLAIM).toString();

            }
        }
        return null;
    }
}
