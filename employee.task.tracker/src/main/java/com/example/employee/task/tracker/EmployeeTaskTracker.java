package com.example.employee.task.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class EmployeeTaskTracker {

    private static ApplicationContext applicationContext;

    public EmployeeTaskTracker() {
    }

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(EmployeeTaskTracker.class);
        app.addInitializers(new DatabaseCreator());
        app.run(args);

    }

}
