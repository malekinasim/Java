package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.service.account.AccountService;
import com.example.employee.task.tracker.service.authprovider.AuthProviderService;
import com.example.employee.task.tracker.service.department.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTTokenProvider jwtTokenProvider;
    private final AuthProviderService authProviderService;
    private final AccountService accountService;
    private final OidcTokenRedisService oidcTokenRedisService;
    private final DepartmentService departmentService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public CustomOAuth2SuccessHandler(
            JWTTokenProvider jwtTokenProvider,
            AuthProviderService authProviderService,
            AccountService accountService,
            OidcTokenRedisService oidcTokenRedisService,
            DepartmentService departmentService,
            OAuth2AuthorizedClientService authorizedClientService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authProviderService = authProviderService;
        this.accountService = accountService;
        this.oidcTokenRedisService = oidcTokenRedisService;
        this.departmentService = departmentService;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (!(authentication instanceof OAuth2AuthenticationToken oauthToken)) {
            response.sendRedirect("/login?error=invalid_auth_type");
            return;
        }

        String registrationId = oauthToken.getAuthorizedClientRegistrationId().toLowerCase(Locale.ROOT);

        AuthProvider authProvider;
        try {
            authProvider = authProviderService.findByRegisterationId(registrationId);
        } catch (CustomException e) {
            response.sendRedirect("/login?error=unknown_provider");
            return;
        }

        OAuth2User oauthUser = (OAuth2User) oauthToken.getPrincipal();
        String email = Optional.ofNullable((String)oauthUser.getAttribute("email"))
                .orElseGet(oauthUser::getName); // fallback if no email provided

        if (email == null || email.isBlank()) {
            response.sendRedirect("/login?error=missing_email");
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
        String externalUserId = oauthUser.getName(); // fallback ID
        if (oauthUser instanceof OidcUser oidcUser) {
            externalUserId = oidcUser.getSubject();
        }

        // Load or create account
        Account account = accountService.findByUserNameAndProvider(email, registrationId)
                .orElseGet(() -> accountService.createOauthProviderAccount(email, authProvider));

        // Save tokens (optional: customize if you don’t want to store all)
        assert expiresAt != null;
        oidcTokenRedisService.saveToken(externalUserId, idToken, accessToken, refreshToken, expiresAt);

        // Create JWT and redirect
        String jwt;

        if (account.getEmployee() == null) {
            jwt = jwtTokenProvider.createAccessToken(account.getUsername(), Employee.Role.EMPLOYEE.name(), authProvider);
            String jwtRefreshToken= jwtTokenProvider.createRefreshToken(account.getUsername());
            response.setHeader("Authorization", "Bearer " + jwt);
            response.setHeader("refreshToken",jwtRefreshToken);
            response.sendRedirect("/fill_account?provider=" + registrationId);
        } else {
            Department currentDP = departmentService.getEmployeeCurrentDepartment(account.getEmployee().getEmployeeNumber());
            jwt = jwtTokenProvider.createAccessToken(account.getUsername(), account.getEmployee().getRole().name(), currentDP.getDepartmentCode());
            String jwtRefreshToken= jwtTokenProvider.createRefreshToken(account.getUsername());
            response.setHeader("Authorization", "Bearer " + jwt);
            response.setHeader("CurrentDepartment", currentDP.getDepartmentCode());
            response.setHeader("refreshToken",jwtRefreshToken);
            response.sendRedirect("/home?provider=" + registrationId);
        }
    }
}
