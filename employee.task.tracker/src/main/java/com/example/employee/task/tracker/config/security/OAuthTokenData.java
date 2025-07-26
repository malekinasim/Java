package com.example.employee.task.tracker.config.security;

import java.time.Instant;

public class OAuthTokenData {
    private final String userId;
    private final String idToken;
    private final String accessToken ;
    private final String refreshToken;
    private final Instant expiresAt;

    public OAuthTokenData(String userId, String idToken, String accessToken, String refreshToken, Instant expiresAt) {
        this.userId = userId;
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
