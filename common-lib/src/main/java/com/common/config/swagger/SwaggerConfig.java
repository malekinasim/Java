package com.common.config.swagger;
import com.common.service.client.AuthProviderClient;
import com.common.service.client.AuthProviderDto;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.*;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    private final AuthProviderClient authProviderClient;
    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    public SwaggerConfig(AuthProviderClient authProviderClient) {
        this.authProviderClient = authProviderClient;
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
        List<AuthProviderDto> providerList = authProviderClient.fetchAllProvidersByType(AuthProviderDto.ProviderType.OIDC);
        if (!CollectionUtils.isEmpty(providerList)) {
            providerList.stream()
                    .forEach(provider -> {
                        // Build OAuth flow
                        OAuthFlow flow = new OAuthFlow()
                                .authorizationUrl(provider.getAuthorizationUri())
                                .tokenUrl(provider.getTokenUri())
                                .scopes(parseScopes(provider.getScopes()));
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


    @Bean
    public OpenApiCustomizer globalHeaderCustomiser() {
        return openApi -> {
            Paths paths = openApi.getPaths();
            if (paths != null) {
                paths.forEach((path, pathItem) -> {
                    for (PathItem.HttpMethod method : pathItem.readOperationsMap().keySet()) {
                        Operation operation = pathItem.readOperationsMap().get(method);
                        if (operation != null) {
                            Parameter apiClientHeader = new HeaderParameter()
                                    .name("X-API-CLIENT")
                                    .description("API Client Request")
                                    .required(true);
                            operation.addParametersItem(apiClientHeader);
                        }
                    }
                });
            }
        };
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
