package com.example.employee.task.tracker.config.security;


public class OauthTemp {
    private final String username;
    private final String provider;
    private final String externalUserId;

    public OauthTemp(String username, String provider, String externalUserId) {
        this.username = username;
        this.provider = provider;
        this.externalUserId = externalUserId;
    }

    public String getUsername() { return username; }
    public String getProvider() { return provider; }
    public String getExternalUserId() { return externalUserId; }
}
