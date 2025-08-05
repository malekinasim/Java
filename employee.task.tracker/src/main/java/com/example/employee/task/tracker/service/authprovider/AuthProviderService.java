package com.example.employee.task.tracker.service.authprovider;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.service.BaseService;

import java.util.List;


public interface AuthProviderService extends BaseService<AuthProvider,Long> {
    AuthProvider findByRegisterationId(String authProvider);

    long countActive();

    void saveAll(List<AuthProvider> authProviders);
}
