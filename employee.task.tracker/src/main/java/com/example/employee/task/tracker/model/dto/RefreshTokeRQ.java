package com.example.employee.task.tracker.model.dto;

public class RefreshTokeRQ {
    private String refreshToken;
    private String CurrentDepartment;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getCurrentDepartment() {
        return CurrentDepartment;
    }

    public void setCurrentDepartment(String currentDepartment) {
        CurrentDepartment = currentDepartment;
    }
}
