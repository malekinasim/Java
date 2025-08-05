package com.example.employee.task.tracker.controller;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.config.security.CustomUserDetail;
import com.example.employee.task.tracker.config.security.JWTTokenProvider;
import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.model.dto.LoginRQ;
import com.example.employee.task.tracker.model.dto.LoginResponse;
import com.example.employee.task.tracker.model.dto.SignupRQ;
import com.example.employee.task.tracker.service.account.AccountService;
import com.example.employee.task.tracker.service.department.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AccountController {
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final DepartmentService departmentService;

    public AccountController(AccountService accountService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, DepartmentService departmentService) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.departmentService = departmentService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> SignUp(@RequestBody SignupRQ signupRQ) {
        accountService.createActiveAccount(signupRQ);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> SignIn(@RequestBody LoginRQ loginRQ) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRQ.getUserName(),
                            loginRQ.getPassword()));
        } catch (DisabledException e) {
            throw new CustomException("error.user_disabled");
        } catch (BadCredentialsException e) {
            throw new CustomException("error.invalid_credential");
        }
        CustomUserDetail userDetail=(CustomUserDetail) authentication.getPrincipal();
        String token = jwtTokenProvider.createAccessToken(loginRQ.getUserName(), userDetail.getEmployee().getRole().name(), userDetail.getCurrentUserDepartment().getDepartmentCode());
        String refreshToken = jwtTokenProvider.createRefreshToken(loginRQ.getUserName());

        return new ResponseEntity<>(new LoginResponse(userDetail.getCurrentUserDepartment().getDepartmentCode(),refreshToken, token), HttpStatus.OK);

    }

    @GetMapping("/refresh-token/{username}")
    public ResponseEntity<?> refreshToken(@PathVariable String username) {
        if (username != null) {
            String refreshToken = jwtTokenProvider.getRefreshToken(username);
            if (StringUtils.hasText(refreshToken)) {
                Account account = accountService.findByUserName(username);
                Department currentDP = departmentService.getEmployeeCurrentDepartment(account.getEmployee().getEmployeeNumber());
                String token = jwtTokenProvider.createAccessToken(username, account.getEmployee().getRole().name(), currentDP.getDepartmentCode());
                return new ResponseEntity<>(new LoginResponse(currentDP.getDepartmentCode(), refreshToken,token), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>( HttpStatus.EXPECTATION_FAILED);
    }


    @GetMapping(value = "/valid-jwt")
    public ResponseEntity<?> isValid(HttpServletRequest request) {
        String jwtToken = jwtTokenProvider.resolveAccessToken(request);

        if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>( HttpStatus.EXPECTATION_FAILED);

    }

    @GetMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        return new ResponseEntity<>( HttpStatus.EXPECTATION_FAILED);

    }

}
