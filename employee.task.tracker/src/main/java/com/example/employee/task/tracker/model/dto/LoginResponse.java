package com.example.employee.task.tracker.model.dto;

public class LoginResponse {
    private String token;
    private String DepartmentCode;

    public LoginResponse(String token, String departmentCode) {
        this.token = token;
        DepartmentCode = departmentCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        DepartmentCode = departmentCode;
    }
}
