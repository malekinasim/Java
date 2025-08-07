package com.example.employee.task.tracker.controller;

import com.example.employee.task.tracker.config.RestUtil;
import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.config.security.CustomUserDetail;
import com.example.employee.task.tracker.config.security.JWTTokenProvider;
import com.example.employee.task.tracker.config.security.OauthTemp;
import com.example.employee.task.tracker.config.security.OidcTokenRedisService;
import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.model.dto.LoginRQ;
import com.example.employee.task.tracker.model.dto.LoginResponse;
import com.example.employee.task.tracker.model.dto.SignupRQ;
import com.example.employee.task.tracker.service.account.AccountService;
import com.example.employee.task.tracker.service.department.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AccountController {
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final DepartmentService departmentService;
    private final OidcTokenRedisService oidcTokenRedisService;
    @Value("app.frontend.url")
    private String frontUrl;
    public AccountController(AccountService accountService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, DepartmentService departmentService, OidcTokenRedisService oidcTokenRedisService) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.departmentService = departmentService;
        this.oidcTokenRedisService = oidcTokenRedisService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> SignUp(@RequestBody SignupRQ signupRQ) {
        accountService.createActiveAccount(signupRQ);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/oauth/complete-registration")
    public ResponseEntity<?> completeRegistration(@RequestBody SignupRQ signupRQ) {
        OauthTemp temp = oidcTokenRedisService.getAuthRequest( signupRQ.getAuthProvider(),signupRQ.getUserName());
        if (temp == null) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CustomException("error.complete-registration.deadline.end"));
        // Update user Employee in DB
        accountService.createActiveAccount(signupRQ);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/login")
    public void signIn(@RequestBody LoginRQ loginRQ, HttpServletRequest request, HttpServletResponse response) throws IOException, IOException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRQ.getUserName(), loginRQ.getPassword()));
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        String departmentCode = userDetail.getCurrentUserDepartment() != null
                ? userDetail.getCurrentUserDepartment().getDepartmentCode()
                : null;
        boolean isApiOrMobile = RestUtil.isApiClient(request) || RestUtil.isMobileClient(request);
        boolean useCookie = !isApiOrMobile;

        jwtTokenProvider.sendTokens( response,departmentCode,  useCookie,loginRQ.getUserName(),userDetail.getEmployee().getRole().name());

        if (!isApiOrMobile) {
            response.sendRedirect(frontUrl + "/home");
        }
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
