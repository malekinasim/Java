package com.example.employee.task.tracker.model;


import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee extends BaseEntity<Long> {

    @Column(name = "password", nullable = false)
    private String password;
    @Enumerated
    @Column(name = "Role",nullable = true )
    private Role role;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "family", nullable = false)
    private String family;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "employee_number", nullable = false, unique = true)
    private String employeeNumber;

    @Formula("name || ' ' || family")
    private String fullName;

    @Column(name = "start_date", nullable = false)
    private LocalDate StartDate;

    @Column(name = "end_date")
    private LocalDate EndDate;

    // One employee can be responsible for many tasks
    @OneToMany(mappedBy = "responsible", fetch = FetchType.LAZY)
    private List<Task> tasks;


    @OneToMany(mappedBy = "id",targetEntity = DepartmentEmployeesHistory.class)
    Set<DepartmentEmployeesHistory> departmentEmployeesHistories;

    @OneToMany(mappedBy = "id",targetEntity = Account.class)
    Set<Account> accounts;

    // --- Getters & Setters ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public LocalDate getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDate startDate) {
        StartDate = startDate;
    }

    public LocalDate getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDate endDate) {
        EndDate = endDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public Set<DepartmentEmployeesHistory> getDepartmentEmployeesHistories() {
        return departmentEmployeesHistories;
    }

    public void setDepartmentEmployeesHistories(Set<DepartmentEmployeesHistory> departmentEmployeesHistories) {
        this.departmentEmployeesHistories = departmentEmployeesHistories;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeNumber, employee.employeeNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeNumber);
    }

    @Override
    public String toString() {
        return "Employee{" +
                ", EndDate=" + EndDate +
                ", StartDate=" + StartDate +
                ", fullName='" + fullName + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", family='" + family + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
    public enum Role{
        ADMIN,EMPLOYEE
    }

}
