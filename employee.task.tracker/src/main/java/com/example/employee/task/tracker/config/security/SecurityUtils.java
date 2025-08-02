package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.service.account.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {
    private static AccountService accountService = null;

    private SecurityUtils(AccountService accountService) {
        this.accountService = accountService;
    }

    public static Authentication getAuthentication() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        return authentication;
    }
    private static CustomUserDetail  getCurrentPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetail)) {
            return null;
        }
        return (CustomUserDetail) principal;
    }
    public static Optional<Employee> getCurrentEmployee() {
        CustomUserDetail customUserDetail = getCurrentPrincipal();
        if (customUserDetail ==null || customUserDetail.getEmployee() == null) {
            return Optional.empty();
        }
        return Optional.of(customUserDetail.getEmployee());
    }

    public static Optional<Account> getCurrentAccount() {

        CustomUserDetail customUserDetail = getCurrentPrincipal();
        if (customUserDetail ==null || customUserDetail.getAccount() == null) {
            return Optional.empty();
        }
        return Optional.of(customUserDetail.getAccount());
    }

    public static String getCurrentUserName() {
        CustomUserDetail customUserDetail = getCurrentPrincipal();
        if (customUserDetail ==null || customUserDetail.getAccount() == null) {
            return null;
        }
        return customUserDetail.getAccount().getUsername();
    }
}
