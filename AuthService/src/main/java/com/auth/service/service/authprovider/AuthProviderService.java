package com.auth.service.service.authprovider;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.service.BaseService;

import java.util.List;


public interface AuthProviderService extends BaseService<AuthProvider,Long> {
    AuthProvider findByRegistrationId(String authProvider);

    long countActive();

    void saveAll(List<AuthProvider> authProviders);

    List<AuthProvider> findAllByType(AuthProvider.ProviderType providerType);
}
