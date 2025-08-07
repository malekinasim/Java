package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.AuthProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OidcTokenRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final OidcTokenClient oidcTokenClient;

    private static final String OIDC_KEY_PREFIX = "oidc_token:";
    private static final String AUTH_REQ_PREFIX = "oauth_req:"; // authRequestId prefix

    public OidcTokenRedisService(RedisTemplate<String, String> redisTemplate,
                                 OidcTokenClient oidcTokenClient) {
        this.redisTemplate = redisTemplate;
        this.oidcTokenClient = oidcTokenClient;
    }
    public OAuthTokenData saveToken(String userId,
                                    String idToken,
                                    String accessToken,
                                    String refreshToken,
                                    Instant accessTokenExpiresAt) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("userId is required");
        }
        if (accessTokenExpiresAt == null) {
            throw new IllegalArgumentException("accessTokenExpiresAt is required");
        }

        String key = OIDC_KEY_PREFIX + userId;
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id_token", idToken != null ? idToken : "");
        tokenMap.put("access_token", accessToken != null ? accessToken : "");
        tokenMap.put("refresh_token", refreshToken != null ? refreshToken : "");
        tokenMap.put("expires_at", String.valueOf(accessTokenExpiresAt.toEpochMilli()));

        redisTemplate.opsForHash().putAll(key, tokenMap);

        long ttlMillis = accessTokenExpiresAt.toEpochMilli() - Instant.now().toEpochMilli();
        if (ttlMillis <= 0) {
            // token already expired — remove and throw
            redisTemplate.delete(key);
            throw new CustomException("error.token.already_expired");
        }
        redisTemplate.expire(key, ttlMillis, TimeUnit.MILLISECONDS);

        return new OAuthTokenData(userId, idToken, accessToken, refreshToken, accessTokenExpiresAt);
    }

    public OAuthTokenData getToken(String userId) {
        if (!tokenExists(userId)) {
            throw new CustomException("error.token.not_exist");
        }

        String key = OIDC_KEY_PREFIX + userId;
        Map<Object, Object> tokenData = redisTemplate.opsForHash().entries(key);

        String idToken = (String) tokenData.getOrDefault("id_token", "");
        String accessToken = (String) tokenData.getOrDefault("access_token", "");
        String refreshToken = (String) tokenData.getOrDefault("refresh_token", "");
        String expiresAtStr = (String) tokenData.get("expires_at");

        if (!StringUtils.hasText(expiresAtStr)) {
            throw new CustomException("error.token.invalid_expires");
        }

        Instant expiresAt;
        try {
            long epochMillis = Long.parseLong(expiresAtStr);
            expiresAt = Instant.ofEpochMilli(epochMillis);
        } catch (NumberFormatException ex) {
            throw new CustomException("error.token.invalid_expires_format");
        }

        return new OAuthTokenData(userId, idToken, accessToken, refreshToken, expiresAt);
    }

    public boolean isAccessTokenExpired(String userId) {
        if (!tokenExists(userId)) return true;
        String key = OIDC_KEY_PREFIX + userId;
        String expiresAtStr = (String) redisTemplate.opsForHash().get(key, "expires_at");
        if (!StringUtils.hasText(expiresAtStr)) return true;
        try {
            long epochMillis = Long.parseLong(expiresAtStr);
            Instant expiresAt = Instant.ofEpochMilli(epochMillis);
            return Instant.now().isAfter(expiresAt);
        } catch (NumberFormatException ex) {
            return true;
        }
    }

    public void deleteToken(String userId) {
        redisTemplate.delete(OIDC_KEY_PREFIX + userId);
    }

    public boolean tokenExists(String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(OIDC_KEY_PREFIX + userId));
    }

    public OAuthTokenData refreshAccessToken(OAuthTokenData oldToken, AuthProvider provider) {
        if (oldToken == null) throw new IllegalArgumentException("oldToken is required");
        OAuthTokenData response = oidcTokenClient.refreshAccessToken(provider, oldToken.getRefreshToken(), oldToken.getUserId());
        if (response == null) {
            throw new CustomException("error.token.refresh_failed");
        }

        // Determine new expiry
        Instant newExpiresAt = response.getExpiresAt();
        if (newExpiresAt == null) {
            Long expiresInSeconds = response.getExpiresInSeconds(); // may be null
            if (expiresInSeconds != null && expiresInSeconds > 0) {
                newExpiresAt = Instant.now().plusSeconds(expiresInSeconds);
            } else {
                throw new CustomException("error.token.invalid_refresh_response");
            }
        }

        String refreshTokenToStore = StringUtils.hasText(response.getRefreshToken())
                ? response.getRefreshToken()
                : oldToken.getRefreshToken();

        return saveToken(oldToken.getUserId(),
                response.getIdToken(),
                response.getAccessToken(),
                refreshTokenToStore,
                newExpiresAt);
    }

    public void saveAuthRequest(String authRequestId, OauthTemp temp, Duration ttl) {
        if (!StringUtils.hasText(authRequestId) || temp == null) {
            throw new IllegalArgumentException("authRequestId and temp are required");
        }
        String key = AUTH_REQ_PREFIX + authRequestId+temp.getUsername();
        Map<String, String> map = new HashMap<>();
        map.put("username", temp.getUsername() == null ? "" : temp.getUsername());
        map.put("provider", temp.getProvider() == null ? "" : temp.getProvider());
        map.put("externalUserId", temp.getExternalUserId() == null ? "" : temp.getExternalUserId());
        // if you need allowedDepartments or other fields, add them here (as JSON or CSV)
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, ttl.toMillis(), TimeUnit.MILLISECONDS);
    }

    public OauthTemp getAuthRequest(String authRequestId,String username) {
        if (!StringUtils.hasText(authRequestId) || !StringUtils.hasText(username)) return null;
        String key = AUTH_REQ_PREFIX + authRequestId+username;
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) return null;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        String n_username = String.valueOf(entries.getOrDefault("username", ""));
        String provider = String.valueOf(entries.getOrDefault("provider", ""));
        String externalUserId = String.valueOf(entries.getOrDefault("externalUserId", ""));
        return new OauthTemp(n_username, provider, externalUserId);
    }

    public void deleteAuthRequest(String authRequestId) {
        if (!StringUtils.hasText(authRequestId)) return;
        redisTemplate.delete(AUTH_REQ_PREFIX + authRequestId);
    }
}
