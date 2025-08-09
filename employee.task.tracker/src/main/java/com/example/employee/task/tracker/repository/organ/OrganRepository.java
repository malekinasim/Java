package com.example.employee.task.tracker.repository.organ;

import com.example.employee.task.tracker.model.Organ;
import com.example.employee.task.tracker.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping

public interface OrganRepository  extends BaseRepository<Organ, Long> {
    @Query(value = "select org from Employee emp " +
            "inner join emp.organ org where emp.employeeNumber = :employeeNumber")
    Optional<Organ> findEmployeeOrgan(String employeeNumber);

    Optional<Organ> findOrganByCode(String employeeNumber);
}
