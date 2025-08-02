package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.model.AuthProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

public class OAuthClientRegistrationFactory {
    public static ClientRegistration create(AuthProvider provider) {
        return ClientRegistration.withRegistrationId(provider.getRegistrationId())
                .clientId(provider.getClientId())
                .clientSecret(provider.getClientSecret())
                .clientAuthenticationMethod(org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(provider.getRedirectUri()) // e.g., "{baseUrl}/login/oauth2/code/{registrationId}"
                .scope(provider.getScopes().split(","))
                .authorizationUri(provider.getAuthorizationUri())
                .tokenUri(provider.getTokenUri())
                .userInfoUri(provider.getUserInfoUri())
                .userNameAttributeName(provider.getUserNameAttribute())
                .clientName(provider.getName())
                .build();
    }
}
