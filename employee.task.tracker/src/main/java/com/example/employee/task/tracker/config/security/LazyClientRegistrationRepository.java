package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.service.authprovider.AuthProviderService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.List;
import java.util.function.Supplier;

public class LazyClientRegistrationRepository implements ClientRegistrationRepository {

    private final InMemoryClientRegistrationRepository delegate;

    public LazyClientRegistrationRepository(AuthProviderService providerService) {


        this.delegate = (InMemoryClientRegistrationRepository) lazyClientRegistrationRepositorySupplier(providerService).get();
    }


    @Bean
    public Supplier<ClientRegistrationRepository> lazyClientRegistrationRepositorySupplier(AuthProviderService providerService) {
        return () -> {
            List<AuthProvider> oauthProviders = providerService.findAll();

            List<ClientRegistration> registrations = oauthProviders.stream()
                    .filter(provider -> provider.getType() == AuthProvider.ProviderType.OIDC)
                    .map(OAuthClientRegistrationFactory::create)
                    .toList();

            return new InMemoryClientRegistrationRepository(registrations);
        };
    }
    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return delegate.findByRegistrationId(registrationId);
    }
}
