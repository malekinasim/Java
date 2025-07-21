package com.example.employee.task.tracker.repoeitory.employee;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.repoeitory.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee, Long> {

    Optional<Employee> findByEmployeeNumber(String employeeNumber);
}
