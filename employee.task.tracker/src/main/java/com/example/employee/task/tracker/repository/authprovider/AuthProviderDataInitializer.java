package com.example.employee.task.tracker.repository.authprovider;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.service.authprovider.AuthProviderService;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AuthProviderDataInitializer {

    public void initializeAuthProviders(AuthProviderService authProviderService) {

            if (authProviderService.countActive() == 0) {
                AuthProvider local = new AuthProvider();
                local.setType(AuthProvider.ProviderType.LOCAL);
                local.setName("LOCAL");
                local.setRegistrationId("local");

                AuthProvider google = new AuthProvider();
                google.setName("GOOGLE");
                google.setRegistrationId("google");
                google.setType(AuthProvider.ProviderType.OIDC);
                google.setClientId("551581295635-f0tccrjv7d285nso5tndhtv3pn565hqi.apps.googleusercontent.com");
                google.setClientSecret("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");
                google.setRedirectUri("http://localhost:8080/login/oauth2/code/google");
                google.setIssuerUri("https://accounts.google.com");
                google.setJwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
                google.setTokenUri("https://oauth2.googleapis.com/token");
                google.setRefreshUri("https://oauth2.googleapis.com/token");
                google.setAuthorizationUri("https://oauth2.googleapis.com/token");
                google.setUserNameAttribute("sub");
                google.setScopes("profile,email,openid");
                authProviderService.saveAll(List.of(local,google));
                System.out.println("✅ AuthProviders inserted.");
            }

    }
}
