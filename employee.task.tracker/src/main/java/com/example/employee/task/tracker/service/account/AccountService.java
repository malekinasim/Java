package com.example.employee.task.tracker.service.account;

import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.dto.SignupRQ;
import com.example.employee.task.tracker.service.BaseService;

public interface AccountService extends BaseService<Account, Long> {
    Account findByUserName(String userName);
    void createAccount(SignupRQ signupRQ);
}
