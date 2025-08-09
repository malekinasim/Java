package com.example.employee.task.tracker.repository.authprovider;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthProviderRepository extends BaseRepository<AuthProvider, Long> {
    Optional<AuthProvider> findByRegistrationId(String name);

}
