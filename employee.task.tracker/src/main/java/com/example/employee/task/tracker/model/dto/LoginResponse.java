package com.example.employee.task.tracker.model.dto;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String DepartmentCode;

    public LoginResponse( String departmentCode, String refreshToken,String accessToken ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        DepartmentCode = departmentCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        DepartmentCode = departmentCode;
    }
}
