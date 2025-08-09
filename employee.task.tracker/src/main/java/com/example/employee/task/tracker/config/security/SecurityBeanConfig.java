package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.repository.authprovider.AuthProviderDataInitializer;
import com.example.employee.task.tracker.service.authprovider.AuthProviderService;
import com.example.employee.task.tracker.service.organ.OrganService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityBeanConfig {
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JWTTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final Environment environment;
    private final AuthProviderService providerService;
    private final AuthProviderDataInitializer authProviderDataInitializer;
    private final OrganService organService;

    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/public/**",
            "/api/v1/auth/**"
    };

    public SecurityBeanConfig(@Lazy CustomOAuth2SuccessHandler customOAuth2SuccessHandler,
                              JWTTokenProvider jwtTokenProvider,
                              CustomUserDetailsService customUserDetailsService,
                              Environment environment,
                              AuthProviderService providerService,
                              AuthProviderDataInitializer authProviderDataInitializer,
                              OrganService organService) {
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.environment = environment;
        this.providerService = providerService;
        this.authProviderDataInitializer = authProviderDataInitializer;
        this.organService = organService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Lazy
    public ClientRegistrationRepository clientRegistrationRepository() {
        authProviderDataInitializer.initializeAuthProviders(providerService);
        List<AuthProvider> oauthProviders = providerService.findAll();

        List<ClientRegistration> registrations = oauthProviders.stream()
                .filter(provider -> provider.getType() == AuthProvider.ProviderType.OIDC)
                .map(OAuthClientRegistrationFactory::create)
                .toList();

        return new InMemoryClientRegistrationRepository(registrations);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        ClientRegistrationRepository repo = this.clientRegistrationRepository();
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(repo)
                        .authorizedClientService(authorizedClientService(repo))
                        .successHandler(customOAuth2SuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .permitAll()
                )
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JWTAuthenticationFilter tokenAuthenticationFilter() {
        return new JWTAuthenticationFilter(jwtTokenProvider, customUserDetailsService, List.of(AUTH_WHITELIST),organService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                Objects.requireNonNull(environment.getProperty("cors.allowedOrigins")).split(",")
        ));
        config.setAllowedMethods(Arrays.asList(
                Objects.requireNonNull(environment.getProperty("cors.allowedMethods")).split(",")
        ));
        config.setAllowedHeaders(Arrays.asList(
                Objects.requireNonNull(environment.getProperty("cors.allowedHeaders")).split(",")
        ));
        config.setMaxAge(Long.parseLong(Objects.requireNonNull(environment.getProperty("cors.maxAge"))));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
