package com.auth.service.config.security;

import com.example.employee.task.tracker.model.AuthProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

public class OAuthClientRegistrationFactory {

    public static ClientRegistration create(AuthProvider provider) {
        return ClientRegistration.withRegistrationId(provider.getRegistrationId())
                .clientId(provider.getClientId())
                .clientSecret(provider.getClientSecret())
                .clientAuthenticationMethod(org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(provider.getRedirectUri())
                .scope(provider.getScopes().split(","))
                .authorizationUri(provider.getAuthorizationUri())
                .tokenUri(provider.getTokenUri())
                .issuerUri(provider.getIssuerUri())
                .jwkSetUri(provider.getJwkSetUri())
                .userInfoUri(provider.getUserInfoUri())
                .userNameAttributeName(provider.getUserNameAttribute())
                .clientName(provider.getName())
                .build();
    }
}
