package com.example.employee.task.tracker.service.employee;

import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.model.dto.EmployeeDto;
import com.example.employee.task.tracker.model.dto.SignupRQ;
import com.example.employee.task.tracker.service.BaseService;

import java.util.Optional;

public interface EmployeeService extends BaseService<Employee, Long> {
    EmployeeDto mapToDto(Employee employee);
    Employee mapToEntity(EmployeeDto employeeDto);

    Optional<Employee> findByEmployeeNumber(String employeeNumber);

    Employee mapToDto(SignupRQ signupRQ);
}

