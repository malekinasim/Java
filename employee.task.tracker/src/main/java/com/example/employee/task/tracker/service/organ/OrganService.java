package com.example.employee.task.tracker.service.organ;

import com.example.employee.task.tracker.model.Organ;
import com.example.employee.task.tracker.service.BaseService;

public interface OrganService extends BaseService<Organ,Long> {
    Organ getEmployeeOrgan(String employeeNumber);

    Organ getOrganByCode(String organCode);
}
