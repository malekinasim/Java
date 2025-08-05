package com.example.employee.task.tracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "auth_providers")
public class AuthProvider extends BaseEntity<Long>{

    @Column(name = "registration_id", unique = true, nullable = false)
    private String registrationId; // e.g., "LOCAL", "GOOGLE", "MICROSOFT"

    @Column(name = "name")
    private String name;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "client_id")
    private  String clientId;

    @Column(name = "client_secret")
    private  String clientSecret ;

    @Column(name = "token_uri")
    private String tokenUri;

    @Column(name = "issue_uri")
    private String issuerUri;

    @Column(name = "scope")
    private String scopes;
    @Column(name = "redirect_uri")
    private String redirectUri;

    @Column(name = "userinfo_uri")
    private String userInfoUri;

    @Column(name = "authorization_uri")
    private String authorizationUri;

    @Column(name = "usernam_attr")
    private String userNameAttribute;
    @Enumerated
    @Column(name = "type")
    private ProviderType type;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String name) {
        this.registrationId = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String displayName) {
        this.name = displayName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getIssuerUri() {
        return issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String getUserNameAttribute() {
        return userNameAttribute;
    }

    public void setUserNameAttribute(String userNameAttribute) {
        this.userNameAttribute = userNameAttribute;
    }

    public ProviderType getType() {
        return type;
    }

    public void setType(ProviderType type) {
        this.type = type;
    }

    public enum ProviderType {
        LOCAL,LDAP,OIDC
    }
}
