package com.example.employee.task.tracker.repoeitory.account;

import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.repoeitory.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account,Long> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByUsernameAndProvider_Name(String username,String ProviderName);
}
