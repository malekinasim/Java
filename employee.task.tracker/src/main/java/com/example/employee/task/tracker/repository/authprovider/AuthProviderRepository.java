package com.example.employee.task.tracker.repository.authprovider;

import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthProviderRepository extends BaseRepository<AuthProvider, Long> {
    Optional<AuthProvider> findByRegistrationId(String name);
    @Query(value = "select pr from AuthProvider pr where pr.type= :type")
    List<AuthProvider> getAllByType(AuthProvider.ProviderType type);
}
