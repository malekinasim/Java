package com.example.employee.task.tracker.model.dto;

import com.example.employee.task.tracker.model.Task;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDto {

    @NotBlank(message = "employee.name.required")
    private String name;

    @NotBlank(message = "employee.family.required")
    private String family;

    @NotBlank(message = "employee.phoneNumber.required")
    private String phoneNumber;


    private String address;

    private String employeeNumber;

    private String fullName;

    private LocalDate StartDate;

    private LocalDate EndDate;

    private List<TaskDto> tasks;

    public @NotBlank(message = "employee.name.required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "employee.name.required") String name) {
        this.name = name;
    }

    public @NotBlank(message = "employee.family.required") String getFamily() {
        return family;
    }

    public void setFamily(@NotBlank(message = "employee.family.required") String family) {
        this.family = family;
    }

    public @NotBlank(message = "employee.phoneNumber.required") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "employee.phoneNumber.required") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDate startDate) {
        StartDate = startDate;
    }

    public LocalDate getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDate endDate) {
        EndDate = endDate;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }
}
