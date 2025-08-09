package com.example.employee.task.tracker.config.security;

import com.example.employee.task.tracker.model.Account;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.model.Organ;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


public class CustomUserDetail  implements UserDetails{

    @Hidden
    private Account account;

    @Hidden
    private Employee employee;

    @Hidden
    private Organ organ = null;

    @Hidden
    private Map<String, Object> attributes;

    public CustomUserDetail(Account account) {
        this(account,null);
    }
    public CustomUserDetail(Account account, HashMap<String, Object> attributes) {
        this.account = account;
        this.employee =account.getEmployee();
        this.organ = account.getOrgan();
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //we just add organ based and app based role authority and organ authority and resource authority based on organ and app role
        if (account != null && employee!=null) {

            if (employee.getRole() != null) {
                    GrantedAuthority roleGrantedAuthority = new SimpleGrantedAuthority(employee.getRole().name());
                    grantedAuthorities.add(roleGrantedAuthority);
            }
            if (organ != null ) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ORG_" + organ.getCode()                                        );
                grantedAuthorities.add(grantedAuthority);
            }

        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmployeeNumber();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Organ getOrgan() {
        return organ;
    }

    public void setOrgan(Organ organ) {
        this.organ = organ;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}

