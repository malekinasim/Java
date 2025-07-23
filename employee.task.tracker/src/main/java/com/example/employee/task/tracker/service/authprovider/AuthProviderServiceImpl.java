package com.example.employee.task.tracker.service.authprovider;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.repoeitory.authprovider.AuthProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthProviderServiceImpl implements AuthProviderService {
    private final AuthProviderRepository authProviderRepository;

    public AuthProviderServiceImpl(AuthProviderRepository authProviderRepository) {
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
        return List.of();
    }

    @Override
    public AuthProvider save(AuthProvider authProvider) {
        return null;
    }

    @Override
    public AuthProvider update(AuthProvider authProvider) {
        return null;
    }

    @Override
    public AuthProvider findByName(String name) {
        return authProviderRepository.findByName(name).orElseThrow(()->
                new CustomException("authProvider.not_found.error"));
    }
}
