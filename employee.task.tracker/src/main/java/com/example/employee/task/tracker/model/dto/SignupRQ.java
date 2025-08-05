package com.example.employee.task.tracker.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRQ {
    @NotBlank(message = "signup.userName.required")
    private String userName;

    @NotBlank(message = "signup.password.required")
    private String password;

    @Email(message = "signup.email.valid")
    private String email;

    @NotBlank(message = "signup.employeeNumber.required")
    private String employeeNumber;

    @NotBlank(message = "signup.name.required")
    private String name;

    @NotBlank(message = "signup.family.required")
    private String family;

    @Size(message = "signup.phone_number.required",max = 10)
    private String phone_number;

    @NotBlank(message = "signup.authProvider.required")
    private String authProvider;

    private String address;
     @NotBlank(message = "signup.departmentCode.required")
    private String DepartmentCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public @NotBlank(message = "signup.authProvider.required") String getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(@NotBlank(message = "signup.authProvider.required") String authProvider) {
        this.authProvider = authProvider;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        DepartmentCode = departmentCode;
    }
}
