package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.model.AuthProvider;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
import java.util.Map;

@Component
public class OidcTokenClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public OAuthTokenData refreshAccessToken(AuthProvider provider, String refreshToken,String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "client_id=" + provider.getClientId() +
                "&client_secret=" + provider.getClientSecret() +
                "&refresh_token=" + refreshToken +
                "&grant_type=refresh_token";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                provider.getTokenUri(),
                HttpMethod.POST,
                entity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to refresh access token from provider: " + provider.getRegistrationId());
        }

        Map responseBody = response.getBody();

        assert responseBody != null;
        return new OAuthTokenData(
                userId,
                (String) responseBody.get("access_token"),
                (String) responseBody.get("id_token"),
                refreshToken,
                Instant.parse(((String)  responseBody.get("expires_in")))
        );
    }
}
