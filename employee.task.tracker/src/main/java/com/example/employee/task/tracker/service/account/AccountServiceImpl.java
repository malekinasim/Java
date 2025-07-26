package com.example.employee.task.tracker.service.account;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.AuthProvider;
import com.example.employee.task.tracker.model.BaseEntity;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.model.dto.SignupRQ;
import com.example.employee.task.tracker.repoeitory.account.AccountRepository;
import com.example.employee.task.tracker.service.authprovider.AuthProviderService;
import com.example.employee.task.tracker.service.employee.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService employeeService;
    private final AuthProviderService authProviderService;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, EmployeeService employeeService, AuthProviderService authProviderService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeService = employeeService;
        this.authProviderService = authProviderService;
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
    public void createActiveAccount(SignupRQ signupRQ) {
        Account account = accountRepository.findByUsername(signupRQ.getUserName())
                .orElseGet(() -> {
                            Account account1 = new Account();
                            account1.setUsername(signupRQ.getUserName());
                            account1.setPassword(passwordEncoder.encode(signupRQ.getPassword()));
                            account1.setStatus(BaseEntity.Status.ACTIVE);
                            return account1;
                        });


        Employee employee = null;
        try {
            employee = employeeService.findByEmployeeNumber(signupRQ.getEmployeeNumber());
        } catch (CustomException e) {
            employee = new Employee();
            employee.setName(signupRQ.getName());
            employee.setFamily(signupRQ.getFamily());
            employee.setAddress(signupRQ.getAddress());
            employee.setEmail(signupRQ.getEmail());
            employee.setRole(Employee.Role.EMPLOYEE);
            employee.setStartDate(LocalDate.now());
            employee.setStatus(BaseEntity.Status.ACTIVE);
            employee.setPhoneNumber(signupRQ.getPhone_number());
        }
        account.setEmployee(employee);
        AuthProvider authProvider = authProviderService.findByName(signupRQ.getAuthProvider());
        account.setProvider(authProvider);
        account.setStatus(BaseEntity.Status.ACTIVE);
        this.save(account);
    }

    @Override
    public Account createOauthProviderAccount(String userName, AuthProvider authProvider) {
        Account account= new Account();
        account.setUsername(userName);
        account.setStatus(BaseEntity.Status.ACTIVE);
        account.setProvider(authProvider);
        return account;
    }

    @Override
    public Optional<Account> findByUserNameAndProvider(String userName, String providerName) {
        return accountRepository.findByUsernameAndProvider_Name(userName, providerName);
    }
}
