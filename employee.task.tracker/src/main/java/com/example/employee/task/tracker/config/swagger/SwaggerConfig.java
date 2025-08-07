package com.example.employee.task.tracker.config.swagger;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.service.authprovider.AuthProviderService;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    private final AuthProviderService authProviderService;
    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    public SwaggerConfig(AuthProviderService authProviderService) {
        this.authProviderService = authProviderService;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Components components = new Components();

        // JWT security scheme
        components.addSecuritySchemes(SECURITY_SCHEME_NAME,
                new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Employee Task Tracker API")
                        .version("1.0")
                        .description("API documentation for Employee Task Tracker"))
                .components(components)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));

        // Dynamically add OIDC security schemes
        List<AuthProvider> providerList = authProviderService.findAll();
        if (!CollectionUtils.isEmpty(providerList)) {
            providerList.stream()
                    .filter(provider -> provider.isEnabled() && provider.getType() == AuthProvider.ProviderType.OIDC)
                    .forEach(provider -> {
                        // Build OAuth flow
                        OAuthFlow flow = new OAuthFlow()
                                .authorizationUrl(provider.getAuthorizationUri())
                                .tokenUrl(provider.getTokenUri())
                                .scopes(parseScopes(provider.getScopes()));

                        if (StringUtils.hasText(provider.getRefreshUri())) {
                            flow.refreshUrl(provider.getRefreshUri());
                        }

                        // Add security scheme for this provider
                        components.addSecuritySchemes(provider.getRegistrationId(),
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .flows(new OAuthFlows().authorizationCode(flow)));

                        // Add it as a security requirement too
                        openAPI.addSecurityItem(new SecurityRequirement().addList(provider.getRegistrationId()));
                    });
        }

        return openAPI;
    }

    private Scopes parseScopes(String scopeStr) {
        Scopes scopes = new Scopes();
        if (StringUtils.hasText(scopeStr)) {
            Arrays.stream(scopeStr.split(","))
                    .map(String::trim)
                    .forEach(scope -> scopes.addString(scope, scope + " scope"));
        }
        return scopes;
    }
}
