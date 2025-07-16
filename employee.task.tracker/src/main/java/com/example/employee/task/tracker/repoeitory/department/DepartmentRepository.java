package com.example.employee.task.tracker.repoeitory.department;

import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.repoeitory.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DepartmentRepository extends BaseRepository<Department, Long> {

    @Query(value = "select d from Department d " +
            "inner join  d.departmentEmployeesHistories dh " +
            "inner join dh.employee e " +
            "where e.employeeNumber=:employeeNumber ")
    Optional<Department> getEmployeeCurrentDepartment(String employeeNumber);
}
