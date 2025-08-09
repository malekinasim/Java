package com.example.employee.task.tracker.repository.account;

import com.example.employee.task.tracker.config.hibernate.StatusFilter;
import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.BaseEntity;
import com.example.employee.task.tracker.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account,Long> {
    @StatusFilter(status = BaseEntity.Status.ACTIVE)
    Optional<Account> findByUsername(String username);
    Optional<Account> findByUsernameAndProvider_Name(String username,String ProviderName);
}
