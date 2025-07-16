package com.example.employee.task.tracker.service.employee;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.model.dto.EmployeeDto;
import com.example.employee.task.tracker.repoeitory.employee.EmployeeRepository;
import com.example.employee.task.tracker.service.BaseService;

public interface EmployeeService extends BaseService<Employee, Long, EmployeeRepository> {
    EmployeeDto mapToDto(Employee employee);
    Employee mapToEntity(EmployeeDto employeeDto);
}
