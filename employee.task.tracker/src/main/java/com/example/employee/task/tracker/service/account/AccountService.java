package com.example.employee.task.tracker.service.account;

import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.model.dto.SignupRQ;
import com.example.employee.task.tracker.service.BaseService;

import java.util.Optional;

public interface AccountService extends BaseService<Account, Long> {
    Account findByUserName(String userName);
    void createActiveAccount(SignupRQ signupRQ);

     Optional<Account> findByUserNameAndProvider(String userName, String providerName);
     Account mapToEntity(SignupRQ signupRQ);
    Account createOauthProviderAccount(String userName, AuthProvider authProvider);
}
