package com.example.employee.task.tracker.repository.department;

import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DepartmentRepository extends BaseRepository<Department, Long> {

    @Query(value = "select d from DepartmentEmployees dh " +
            "inner join  dh.department d " +
            "inner join dh.employee e " +
            "where e.employeeNumber=:employeeNumber ")
    Optional<Department> getEmployeeCurrentDepartment(String employeeNumber);


    Optional<Department> findDepartmentByDepartmentCodeAndOrgan_Code(String employeeNumber,String OrganCode);
}
