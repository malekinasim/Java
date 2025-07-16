package com.example.employee.task.tracker.model.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class DepartmentDto {
    @NotBlank(message = "department.code.required")
    private String departmentCode;
    @NotBlank(message = "department.name.required")
    private String name;
    private EmployeeDto manager;
    private Set<DepartmentDto> subDepartment;

    public @NotBlank(message = "department.code.required") String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(@NotBlank(message = "department.code.required") String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public @NotBlank(message = "department.name.required") String getName() {
        return name;
    }
    public void setName(@NotBlank(message = "department.name.required") String name) {
        this.name = name;
    }

    public EmployeeDto getManager() {
        return manager;
    }

    public void setManager(EmployeeDto manager) {
        this.manager = manager;
    }

    public Set<DepartmentDto> getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(Set<DepartmentDto> subDepartment) {
        this.subDepartment = subDepartment;
    }
}
