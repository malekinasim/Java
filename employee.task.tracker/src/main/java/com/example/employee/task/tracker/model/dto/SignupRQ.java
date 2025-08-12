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


    private String authProvider;

    private String oauthReqRqId;

    private String address;

    @NotBlank(message = "signup.organCode.required")
    private String organCode;

     @NotBlank(message = "signup.departmentCode.required")
    private String DepartmentCode;

    public @NotBlank(message = "signup.userName.required") String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank(message = "signup.userName.required") String userName) {
        this.userName = userName;
    }

    public @NotBlank(message = "signup.password.required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "signup.password.required") String password) {
        this.password = password;
    }

    public @Email(message = "signup.email.valid") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "signup.email.valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "signup.employeeNumber.required") String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(@NotBlank(message = "signup.employeeNumber.required") String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public @NotBlank(message = "signup.name.required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "signup.name.required") String name) {
        this.name = name;
    }

    public @NotBlank(message = "signup.family.required") String getFamily() {
        return family;
    }

    public void setFamily(@NotBlank(message = "signup.family.required") String family) {
        this.family = family;
    }

    public @Size(message = "signup.phone_number.required", max = 10) String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(@Size(message = "signup.phone_number.required", max = 10) String phone_number) {
        this.phone_number = phone_number;
    }

    public  String getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public @NotBlank(message = "signup.organCode.required") String getOrganCode() {
        return organCode;
    }

    public void setOrganCode(@NotBlank(message = "signup.organCode.required") String organCode) {
        this.organCode = organCode;
    }

    public @NotBlank(message = "signup.departmentCode.required") String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(@NotBlank(message = "signup.departmentCode.required") String departmentCode) {
        DepartmentCode = departmentCode;
    }

    public String getOauthReqRqId() {
        return oauthReqRqId;
    }

    public void setOauthReqRqId(String oauthReqRqId) {
        this.oauthReqRqId = oauthReqRqId;
    }
}
