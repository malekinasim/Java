package com.example.employee.task.tracker.service.account;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.*;
import com.example.employee.task.tracker.model.dto.SignupRQ;
import com.example.employee.task.tracker.repository.account.AccountRepository;
import com.example.employee.task.tracker.service.authprovider.AuthProviderService;
import com.example.employee.task.tracker.service.employee.EmployeeService;
import com.example.employee.task.tracker.service.organ.OrganService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService employeeService;
    private final AuthProviderService authProviderService;
    private final OrganService organService;

    public AccountServiceImpl(AccountRepository accountRepository, @Lazy PasswordEncoder passwordEncoder, EmployeeService employeeService, AuthProviderService authProviderService, OrganService organService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeService = employeeService;
        this.authProviderService = authProviderService;
        this.organService = organService;
    }


    @Override
    public Account findById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public List<Account> findAll() {
        return List.of();
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        return null;
    }

    @Override
    public Account findByUserName(String userName) {
        return accountRepository.findByUsername(userName).orElseThrow(
                () -> new CustomException("account.not_find.error")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createActiveAccount(SignupRQ signupRQ) {
        Account account = accountRepository.findByUsername(signupRQ.getUserName())
                .orElseGet(() -> mapToEntity(signupRQ));
        Employee newEmployee = employeeService.findByEmployeeNumber(signupRQ.getEmployeeNumber())
                .orElseGet(()->
                        {
                            Employee employee=employeeService.mapToDto(signupRQ);
                            employee.setAccounts(List.of(account));
                            employeeService.save(employee);
                            return employee;
                        }
                );
        account.setEmployee(newEmployee);
        AuthProvider authProvider = authProviderService.findByRegistrationId(signupRQ.getAuthProvider());
        account.setProvider(authProvider);
        account.setStatus(BaseEntity.Status.ACTIVE);
        this.save(account);
    }


    @Override
    public Optional<Account> findByUserNameAndProvider(String userName, String providerName) {
        return accountRepository.findByUsernameAndProvider_RegistrationId(userName, providerName);
    }

    @Override
    public Account mapToEntity(SignupRQ signupRQ) {
        Account account = new Account();
        account.setUsername(signupRQ.getUserName());
        if(signupRQ.getPassword()==null && signupRQ.getOauthReqRqId()==null)
            throw new CustomException("error.password.required");
        if(signupRQ.getPassword()!=null)
         account.setPassword(passwordEncoder.encode(signupRQ.getPassword()));
        account.setStatus(BaseEntity.Status.ACTIVE);
        Organ organ=organService.getOrganByCode(signupRQ.getOrganCode());
        account.setOrgan(organ);
        return account;
    }
}
