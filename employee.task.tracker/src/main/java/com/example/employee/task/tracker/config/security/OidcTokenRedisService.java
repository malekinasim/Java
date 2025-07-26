package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.config.exception.CustomException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class OidcTokenRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOps;

    public OidcTokenRedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    public void saveToken(String userId, String idToken, String accessToken, String refreshToken, Instant accessTokenExpiresAt) {
        String key = "oidc_token:" + userId;

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id_token", idToken);
        tokenMap.put("access_token", accessToken);
        tokenMap.put("refresh_token", refreshToken);
        tokenMap.put("expires_at", accessTokenExpiresAt.toString());
        redisTemplate.opsForHash().putAll(key, tokenMap);
        Duration ttl = Duration.between(Instant.now(), accessTokenExpiresAt);
        redisTemplate.expire(key, ttl);
    }

    public OAuthTokenData getToken(String userId) {
        if(!tokenExists(userId))
            throw new CustomException("error.token.not_exist");
        Map<Object, Object> tokenData = redisTemplate.opsForHash().entries("oidc_token:" + userId);
        String idToken = (String) tokenData.get("id_token");
        String accessToken = (String) tokenData.get("access_token");
        String refreshToken = (String) tokenData.get("refresh_token");
        String expiresAtStr = (String) tokenData.get("expires_at");
        Instant expiresAt = Instant.parse(expiresAtStr);
        return new OAuthTokenData(userId,idToken,accessToken,refreshToken,expiresAt);
    }
    public boolean isAccessTokenExpired(String userId) {
        if (!tokenExists(userId)) return true;

        String expiresAtStr = (String) redisTemplate.opsForHash().get("oidc_token:" + userId, "expires_at");
        assert expiresAtStr != null;
        Instant expiresAt = Instant.parse(expiresAtStr);
        return Instant.now().isAfter(expiresAt);
    }

    public void deleteToken(String userId) {
        redisTemplate.delete("oidc_token:" + userId);
    }

    public boolean tokenExists(String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("oidc_token:" + userId));
    }
}
