package com.example.employee.task.tracker.repoeitory.authprovider;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.repoeitory.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthProviderRepository extends BaseRepository<AuthProvider, Long> {
    Optional<AuthProvider> findByName(String name);
}
