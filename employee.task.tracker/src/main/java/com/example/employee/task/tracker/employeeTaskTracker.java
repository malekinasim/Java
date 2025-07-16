package com.example.employee.task.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class employeeTaskTracker {
    public static void main(String[] args) {
        SpringApplication.run(employeeTaskTracker.class, args);
    }

}
