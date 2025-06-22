package com.example.employee.task.tracker.config.exception;

import org.springframework.http.HttpStatus;

public class StatusCustomException extends CustomException{
    private final HttpStatus status;

    public StatusCustomException(HttpStatus status,String message, Object... args) {
        super(message, args);
        this.status=status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
