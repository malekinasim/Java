package com.common.config.exception;
import com.common.config.resourcebundle.MessageUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
class GlobalExceptionHandler  {
    private  final MessageUtil messageUtil;

    GlobalExceptionHandler(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler(StatusCustomException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(StatusCustomException ex) {
        String message = messageUtil.get(ex.getMessageKey(), ex.getArgs());
        return buildResponse(ex.getStatus(), message+"\n"+LocalDateTime.now());
    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleLocalized(CustomException ex) {
        String message = messageUtil.get(ex.getMessageKey(), ex.getArgs());
        return buildResponse(HttpStatus.BAD_REQUEST, message+"\n"+LocalDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(DataIntegrityViolationException ex) {
        return buildResponse(HttpStatus.CONFLICT,
                "Database constraint violation: " + ex.getRootCause().getMessage());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("errors", errors);

        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse err = new ErrorResponse(status.value(), message, LocalDateTime.now());
        return new ResponseEntity<>(err, status);
    }
}
