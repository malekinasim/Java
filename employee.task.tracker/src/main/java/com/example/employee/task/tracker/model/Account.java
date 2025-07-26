package com.example.employee.task.tracker.model;


import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account extends BaseEntity<Long> {
    @ManyToOne(targetEntity = AuthProvider.class)
    @JoinColumn(name = "provider_id", nullable = false)
    private AuthProvider provider; // LOCAL or GOOGLE

    @Column(unique = true, nullable = false)
    private String username; // can be personnelNumber or Google email

    private String password; // null if oauth provider is used for example ggogle

    @ManyToOne(targetEntity = AuthProvider.class)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
