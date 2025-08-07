package com.example.employee.task.tracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Organ extends BaseEntity<Long>{
    @Column(name = "code",nullable = false)
    private String code;
    @Column(name = "name",nullable = false)
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
