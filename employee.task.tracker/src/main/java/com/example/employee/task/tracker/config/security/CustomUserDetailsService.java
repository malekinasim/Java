package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.service.account.AccountService;
import com.example.employee.task.tracker.service.department.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountService accountService;
    private final DepartmentService departmentService;

    public CustomUserDetailsService(JwtTokenProvider jwtTokenProvider, AccountService accountService, DepartmentService departmentService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountService = accountService;
        this.departmentService = departmentService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String departmentCode = jwtTokenProvider.getCurrentDepartment(request);
        if (departmentCode != null) {
            return loadUserByUsername(username, departmentCode);
        }
        return null;
    }


    private UserDetails loadUserByUsername(String username, String departmentCode) throws UsernameNotFoundException {
        Account account =null;
        try {
             account = accountService.findByUserName(username);
        }catch (CustomException e){
            e.printStackTrace();
        }
            if(account!=null) {
                Department department = departmentService.getEmployeeCurrentDepartment(account.getUsername());
                if (department.getDepartmentCode().equals(departmentCode))
                    return new CustomUserDetail(account, department);
            }
            return null;

    }

}
