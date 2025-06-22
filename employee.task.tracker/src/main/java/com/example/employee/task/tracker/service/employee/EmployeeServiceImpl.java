package com.example.employee.task.tracker.service.employee;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.repoeitory.BaseRepository;
import com.example.employee.task.tracker.repoeitory.employee.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public BaseRepository<Employee, Long> getRepository() {
        return employeeRepository;
    }

    @Override
    public Employee findById(Long id) {
        return getRepository().findById(id).orElseThrow(
                ()->new  CustomException("employee.not_found") );
    }

    @Override
    public void deleteById(Long id) {
         this.getRepository().deleteById(id);
    }

    @Override
    public List<Employee> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Employee save(Employee employee) {
        return ;
    }

    @Override
    public Employee update(Employee employee) {
        return null;
    }
}
