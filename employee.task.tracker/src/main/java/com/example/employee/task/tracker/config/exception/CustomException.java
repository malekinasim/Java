package com.example.employee.task.tracker.config.exception;
public class CustomException extends RuntimeException {
    private final Object[] args;
    private final String messageKey;

    public CustomException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }
    public Object[] getArgs() {
        return args;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
