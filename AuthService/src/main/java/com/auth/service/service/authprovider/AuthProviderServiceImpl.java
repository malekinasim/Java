package com.auth.service.service.authprovider;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.model.BaseEntity;
import com.example.employee.task.tracker.repository.authprovider.AuthProviderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthProviderServiceImpl implements AuthProviderService {
    @PersistenceContext
    private final  EntityManager entityManager;
    private final AuthProviderRepository authProviderRepository;

    public AuthProviderServiceImpl(EntityManager entityManager, AuthProviderRepository authProviderRepository) {
        this.entityManager = entityManager;

        this.authProviderRepository = authProviderRepository;
    }

    @Override
    public AuthProvider findById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public List<AuthProvider> findAll() {
        return authProviderRepository.findAll();
    }

    @Override
    public AuthProvider save(AuthProvider authProvider) {
        authProvider.setStatus(BaseEntity.Status.ACTIVE);
        return authProviderRepository.save(authProvider);
    }

    @Override
    public AuthProvider update(AuthProvider authProvider) {
        return null;
    }

    @Override
    public AuthProvider findByRegistrationId(String name) {
        return authProviderRepository.findByRegistrationId(name).orElseThrow(()->
                new CustomException("authProvider.not_found.error"));
    }

    @Override
    @Transactional
    public long countActive() {


        return authProviderRepository.count(); // Filter will apply
    }

    @Override
    public void saveAll(List<AuthProvider> authProviders) {
        authProviders.forEach(authProvider->authProvider.setStatus(BaseEntity.Status.ACTIVE));
        this.authProviderRepository.saveAll(authProviders);
    }

    @Override
    public List<AuthProvider> findAllByType(AuthProvider.ProviderType providerType) {
        return this.authProviderRepository.getAllByType(providerType);
    }
}
