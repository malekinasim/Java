package com.example.employee.task.tracker.model.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRQ {
    @NotBlank(message = "signup.userName.required")
    private String userName;

    @NotBlank(message = "signup.password.required")
    private String password;


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
}
