package com.auth.service.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {
    @Value("spring.profiles.active")
    private String profile;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private String accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private String refreshTokenExpiration;

    @Value("${jwt.registeraton-token-expiration}")
    private String registerTokenExpiration;

    public final String CUR_ORGAN_CLAIM = "X-Organt-Code";

    public JWTTokenProvider(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public String createAccessToken(String username, String role, String organCode) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        claims.put(CUR_ORGAN_CLAIM, organCode);
        Date now = new Date();
        Date exp = Date.from(Instant.now().plusMillis(Long.parseLong(registerTokenExpiration)));
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
        Date exp = Date.from(Instant.now().plusMillis(Long.parseLong(registerTokenExpiration)));
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
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        // fallback to cookie
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> "ACCESS_TOKEN".equals(c.getName()))
                    .map(Cookie::getValue)
                    .filter(StringUtils::hasText)
                    .findFirst()
                    .orElse(null);
        }
        return null;
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

    public String getCurrentOrgan(HttpServletRequest request) {
        String token = this.resolveAccessToken(request);
        String OrganCode = null;
        if(StringUtils.hasText(token)) {
            Object organClaim = getClaims(token).get(CUR_ORGAN_CLAIM);
            if (organClaim != null && StringUtils.hasText(organClaim.toString())) {
                OrganCode = organClaim.toString();
            } else {
                String headerOrgan = request.getHeader(CUR_ORGAN_CLAIM);
                if (StringUtils.hasText(headerOrgan)) OrganCode = headerOrgan;
            }
        }
        return OrganCode;
    }


    public void sendTokens(HttpServletResponse response,
                           String OrganCode,
                           boolean useCookie,String username, String role) throws IOException {
        String accessJwt = this.createAccessToken(username,role, OrganCode);
        String refreshJwt= this.createRefreshToken(username);
        if (useCookie) {
            // ACCESS cookie
            Cookie accessCookie =  creatTokenCookie("ACCESS_TOKEN", accessJwt, accessTokenExpiration);
            response.addCookie(accessCookie);

            // REFRESH cookie
            Cookie refreshCookie = creatTokenCookie( "REFRESH_TOKEN", refreshJwt, refreshTokenExpiration);
            response.addCookie(refreshCookie);

            if (OrganCode != null) {
                // organ cookie
                response.setHeader(CUR_ORGAN_CLAIM, OrganCode);
            }

        } else {
            // JSON body for API/Mobile clients
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String org = OrganCode == null ? "" : OrganCode;
            String json = String.format("{\"OrganCode\":\"%s\",\"accessToken\":\"%s\",\"refreshToken\":\"%s\"}",
                    escapeJson(org), escapeJson(accessJwt), escapeJson(refreshJwt));
            response.getWriter().write(json);
        }
    }

    private Cookie creatTokenCookie(String name,String value, String tokenExpiration) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        Date expRefreshToken = Date.from(Instant.now().plusMillis(Long.parseLong(tokenExpiration)));
        cookie.setMaxAge((int) Duration.between(Instant.now(), expRefreshToken.toInstant()).getSeconds()       ); // 30 days example
         return  cookie;
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    public enum TokenType{
        REFRESH_TOKEN,ACCESS_TOKEN,REGISTER_TOKEN;
    }
}
