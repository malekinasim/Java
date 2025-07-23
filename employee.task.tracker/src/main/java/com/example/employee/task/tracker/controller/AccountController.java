package com.example.employee.task.tracker.controller;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.config.security.JwtTokenProvider;
import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.model.dto.LoginRQ;
import com.example.employee.task.tracker.model.dto.LoginResponse;
import com.example.employee.task.tracker.model.dto.SignupRQ;
import com.example.employee.task.tracker.service.account.AccountService;
import com.example.employee.task.tracker.service.department.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1")
public class AccountController {
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final DepartmentService departmentService;

    public AccountController(AccountService accountService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, DepartmentService departmentService) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.departmentService = departmentService;
    }

    @PostMapping("/Auth/signup")
    public ResponseEntity<?> SignUp(@RequestBody SignupRQ signupRQ) {
        accountService.createAccount(signupRQ);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/Auth/login")
    public ResponseEntity<?> SignIn(@RequestBody LoginRQ loginRQ) {
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRQ.getUserName(), loginRQ.getPassword()));
        } catch (DisabledException e) {
            throw new CustomException("error.user_disabled");
        } catch (BadCredentialsException e) {
            throw new CustomException("error.invalid_credential");
        }

        Account account = accountService.findByUserName(loginRQ.getUserName());
        Department currentDP = departmentService.getEmployeeCurrentDepartment(account.getEmployee().getEmployeeNumber());
        String token = jwtTokenProvider.createToken(loginRQ.getUserName(), account.getEmployee().getRole().name(), currentDP.getName());


        return new ResponseEntity<>(new LoginResponse(currentDP.getDepartmentCode(), token), HttpStatus.OK);

    }



}
