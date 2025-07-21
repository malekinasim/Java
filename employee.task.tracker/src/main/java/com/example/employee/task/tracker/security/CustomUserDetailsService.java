package com.example.employee.task.tracker.security;

import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.service.department.DepartmentService;
import com.example.employee.task.tracker.service.employee.EmployeeService;
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
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public CustomUserDetailsService(JwtTokenProvider jwtTokenProvider, EmployeeService employeeService, DepartmentService departmentService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.employeeService = employeeService;
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
            Employee employee = employeeService.findByEmployeeNumber(username);
            Department department = departmentService.getEmployeeCurrentDepartment(employee.getEmployeeNumber());
            if (department.getDepartmentCode().equals(departmentCode))
                return new CustomUserDetail(employee, department);
            return null;

    }

}
