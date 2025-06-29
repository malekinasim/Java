package com.example.employee.task.tracker.service.employee;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.model.dto.EmployeeDto;
import com.example.employee.task.tracker.repoeitory.BaseRepository;
import com.example.employee.task.tracker.repoeitory.employee.EmployeeRepository;
import com.example.employee.task.tracker.service.task.TaskService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    @Lazy
    private final TaskService taskService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, TaskService taskService) {
        this.employeeRepository = employeeRepository;
        this.taskService = taskService;
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
        if(employee.getStartDate()==null)
            employee.setStartDate(LocalDate.now());
        return getRepository().save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        Employee find=this.findById(employee.getId());
        find.setName(employee.getName());
        find.setFamily(employee.getFamily());
        find.setStartDate(employee.getStartDate());
        find.setEndDate(employee.getEndDate());
        find.setAddress(employee.getAddress());
        find.setPhoneNumber(employee.getPhoneNumber());
        find.setTasks(employee.getTasks());
        return getRepository().save(find);
    }

    @Override
    public EmployeeDto mapToDto(Employee employee) {
        EmployeeDto employeeDto=new EmployeeDto();
        employeeDto.setName(employee.getName());
        employeeDto.setFamily(employee.getFamily());
        employeeDto.setEmployeeNumber(employee.getEmployeeNumber());
        employeeDto.setAddress(employee.getAddress());
        employeeDto.setFullName(employee.getFullName());
        if(!CollectionUtils.isEmpty(employee.getTasks()))
           employeeDto.setTasks(employee.getTasks().stream().map(taskService::mapToDto).toList());
        employeeDto.setStartDate(employee.getStartDate());
        employeeDto.setEndDate(employee.getEndDate());
        return employeeDto;
    }

    @Override
    public Employee mapToEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setFamily(employeeDto.getFamily());
        employee.setStartDate(employeeDto.getStartDate());
        employee.setEndDate(employeeDto.getEndDate());
        employee.setAddress(employeeDto.getAddress());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());

        if(!CollectionUtils.isEmpty(employeeDto.getTasks()))
          employee.setTasks(employeeDto.getTasks().stream().map(taskService::mapToEntity).toList());

        return employee;
    }
}
