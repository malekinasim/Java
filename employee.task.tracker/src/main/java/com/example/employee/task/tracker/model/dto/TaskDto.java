package com.example.employee.task.tracker.model.dto;


import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
public class TaskDto {
    private String taskNumber;
    @NotBlank(message = "task.name.required")
    private String name;
    private String description;
    @NotBlank(message = "task.startDate.required")
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long responsible_id;
    private EmployeeDto employeeDto;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getResponsible_id() {
        return responsible_id;
    }

    public void setResponsible_id(Long responsible_id) {
        this.responsible_id = responsible_id;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public EmployeeDto getEmployeeDto() {
        return employeeDto;
    }

    public void setEmployeeDto(EmployeeDto employeeDto) {
        this.employeeDto = employeeDto;
    }
}
