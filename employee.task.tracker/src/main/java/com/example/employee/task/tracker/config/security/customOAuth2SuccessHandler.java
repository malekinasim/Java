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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class customOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthProviderService authProviderService;
    private final AccountService accountService;
    private final OidcTokenRedisService oidcTokenRedisService;
    private final DepartmentService departmentService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    public customOAuth2SuccessHandler(JWTTokenProvider jwtTokenProvider, AuthProviderService authProviderService, AccountService accountService, OidcTokenRedisService oidcTokenRedisService, DepartmentService departmentService, OAuth2AuthorizedClientService authorizedClientService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authProviderService = authProviderService;
        this.accountService = accountService;
        this.oidcTokenRedisService = oidcTokenRedisService;
        this.departmentService = departmentService;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            oidcUser.getClaims();
            String email = oidcUser.getEmail();

            // This gives you the registrationId like "google", "github", etc.
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();
            AuthProvider authProvider;
            try{
                authProvider= authProviderService.findByName(registrationId);
            }catch (CustomException e){
                response.sendRedirect("/login?error=unknown_provider");
                return;
            }
            OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    oauthToken.getAuthorizedClientRegistrationId(),
                    oauthToken.getName()
            );

            String oidcAccessToken = client.getAccessToken().getTokenValue();
            String oidcIdToken = oidcUser.getIdToken().getTokenValue();
            String refreshToken= Objects.requireNonNull(client.getRefreshToken()).getTokenValue();
            // 2. Find or register local user
            if(authProvider == null){
                response.sendRedirect("/login?error=unknown_provider");
                return;
            }
            AuthProvider finalAuthProvider = authProvider;
            Account account = accountService.findByUserNameAndProvider(email,authProvider.getName())
                    .orElseGet(() ->
                            accountService.createOauthProviderAccount(email, finalAuthProvider)
                    );

            String userId = oidcUser.getSubject(); // Unique Google user ID
            oidcTokenRedisService.saveToken(userId, oidcIdToken,oidcAccessToken,refreshToken, Objects.requireNonNull(oidcUser.getIdToken().getExpiresAt()));


            // Generate your app JWT token (customize as needed)
            String token;
            if(account.getEmployee()==null) {
                 token = jwtTokenProvider.createToken(account.getUsername(), Employee.Role.EMPLOYEE.name(), authProvider);
                // You can send the JWT as response header or in body
                response.setHeader("Authorization", "Bearer " + token);
                // Optional: redirect to fill_account based  on the provider
                response.sendRedirect("/fill_account?provider=" + registrationId);
            }else {
                Department currentDP = departmentService.getEmployeeCurrentDepartment(account.getEmployee().getEmployeeNumber());
                token = jwtTokenProvider.createToken(account.getUsername(), account.getEmployee().getRole().name(), currentDP.getName());
                // You can send the JWT as response header or in body
                response.setHeader("Authorization", "Bearer " + token);
                // Optional: redirect or issue JWT based on the provider
                response.sendRedirect("/home?provider=" + registrationId);
            }

        } else {
            response.sendRedirect("/login?error=unknown_provider");
        }
        response.sendRedirect("/login?error=unknown_provider");
    }
}
