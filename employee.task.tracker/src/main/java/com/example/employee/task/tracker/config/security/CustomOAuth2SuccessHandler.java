package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.util.RestUtil;
import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.model.Organ;
import com.example.employee.task.tracker.service.account.AccountService;
import com.example.employee.task.tracker.service.authprovider.AuthProviderService;
import com.example.employee.task.tracker.service.organ.OrganService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Value("app.frontend.url")
    private String frontUrl;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthProviderService authProviderService;
    private final AccountService accountService;
    private final OidcTokenRedisService oidcTokenRedisService;
    private final OrganService organService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final Environment env;
    public CustomOAuth2SuccessHandler(
            JWTTokenProvider jwtTokenProvider,
            AuthProviderService authProviderService,
            AccountService accountService,
            OidcTokenRedisService oidcTokenRedisService,
            OrganService organService,
            OAuth2AuthorizedClientService authorizedClientService, Environment env) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authProviderService = authProviderService;
        this.accountService = accountService;
        this.oidcTokenRedisService = oidcTokenRedisService;
        this.organService = organService;
        this.authorizedClientService = authorizedClientService;
        this.env = env;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            response.sendRedirect(frontUrl+"/login?error=invalid_auth_type");
            return;
        }

        String registrationId = oauthToken.getAuthorizedClientRegistrationId().toLowerCase(Locale.ROOT);

        AuthProvider authProvider;
        try {
            authProvider = authProviderService.findByRegisterationId(registrationId);
        } catch (CustomException e) {
            response.sendRedirect(frontUrl +"/login?error=unknown_provider");
            return;
        }



        OAuth2User oauthUser = (OAuth2User) oauthToken.getPrincipal();
        String email = Optional.ofNullable((String)oauthUser.getAttribute("email"))
                .orElseGet(oauthUser::getName); // fallback if no email provided

        if (email == null || email.isBlank()) {
            response.sendRedirect(frontUrl + "/login?error=missing_email");
            return;
        }

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                registrationId,
                oauthToken.getName()
        );

        // Token values
        String accessToken = client.getAccessToken() != null ? client.getAccessToken().getTokenValue() : null;
        String refreshToken = client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : null;
        String idToken = (oauthUser instanceof OidcUser oidcUser) ? oidcUser.getIdToken().getTokenValue() : null;
        Instant expiresAt = (oauthUser instanceof OidcUser oidcUser)
                ? oidcUser.getIdToken().getExpiresAt()
                : client.getAccessToken().getExpiresAt();
        String externalUserId = oauthUser.getName();
        // fallback ID
        if (oauthUser instanceof OidcUser oidcUser) {
            externalUserId = oidcUser.getSubject();
        }
        try {
            if (StringUtils.hasText(externalUserId) && (StringUtils.hasText(idToken) || StringUtils.hasText(accessToken))) {
                Instant effectiveExpiresAt = expiresAt != null ? expiresAt :
                        Instant.now().plus(Duration.ofMinutes(getOauthDefaultExpireTokenSecond()));
                oidcTokenRedisService.saveToken(externalUserId, idToken, accessToken, refreshToken, effectiveExpiresAt);
            }
        } catch (Exception e) {
            throw new CustomException("Failed to persist OIDC tokens for user {}, continuing: {}", externalUserId, e.getMessage());
        }


        // Load or create account
        Account account = accountService.findByUserNameAndProvider(email, registrationId)
                .orElseGet(() -> accountService.createOauthProviderAccount(email, authProvider));

        // --- CASE A: account has no employee (new user) -> create authRequestId and redirect to fill_account ---
        if (account.getEmployee() == null) {
            String authRequestId = UUID.randomUUID().toString();
            // store minimal data in redis
            OauthTemp temp = new OauthTemp(account.getUsername(), registrationId, externalUserId);
            Duration ttl = Duration.ofSeconds(getRegistrationTtlSeconds());
            oidcTokenRedisService.saveAuthRequest(authRequestId, temp, ttl);

            // Decide redirect/response based on client type
            if (RestUtil.isApiClient(request)) {
                // return JSON with authRequestId
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String json = "{\"authRequestId\":\"" + authRequestId + "\" , \"username\":\""+account.getUsername()+"}";
                response.getWriter().write(json);
                return;
            } else {
                // normal web frontend
                String redirect = frontUrl + "/fill_account?authRequestId=" + URLEncoder.encode(authRequestId, StandardCharsets.UTF_8);
                response.sendRedirect(redirect);
                return;
            }
        }
         else {
            Organ organ = organService.getEmployeeOrgan(account.getEmployee().getEmployeeNumber());
            jwtTokenProvider.sendTokens(response, organ.getCode(),!RestUtil.isMobileClient(request),account.getUsername(),account.getEmployee().getEmployeeNumber());
            if (!RestUtil.isApiClient(request)) {
                response.sendRedirect(frontUrl + "/home");
            } else {
                return;
            }

        }
    }
    private long getOauthDefaultExpireTokenSecond() {
        return Long.parseLong(env.getProperty("oauth.access-token-default-expiration", "5"));
    }
    private long getRegistrationTtlSeconds() {
        return Long.parseLong(env.getProperty("app.auth.registration.ttl-seconds", "600"));
    }


}
