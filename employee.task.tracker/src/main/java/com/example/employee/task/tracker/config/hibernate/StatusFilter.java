package com.example.employee.task.tracker.config.hibernate;

import com.example.employee.task.tracker.model.BaseEntity;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StatusFilter {
    BaseEntity.Status status() default BaseEntity.Status.ACTIVE;
}