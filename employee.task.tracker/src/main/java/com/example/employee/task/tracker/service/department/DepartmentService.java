package com.example.employee.task.tracker.service.department;
import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.model.dto.DepartmentDto;
import com.example.employee.task.tracker.repoeitory.department.DepartmentRepository;
import com.example.employee.task.tracker.service.BaseService;

import java.util.Optional;

public interface DepartmentService extends BaseService<Department, Long, DepartmentRepository> {
    Optional<Department> getEmployeeCurrentDepartment(String employeeNumber);

    DepartmentDto mapToDto(Department department);

    Department mapToEntity(DepartmentDto departmentDto);
}
