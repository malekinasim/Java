package com.example.employee.task.tracker.service.employee;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.config.hibernate.StatusFilter;
import com.example.employee.task.tracker.model.BaseEntity;
import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.model.DepartmentEmployees;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.model.dto.EmployeeDto;
import com.example.employee.task.tracker.model.dto.SignupRQ;
import com.example.employee.task.tracker.repoeitory.employee.EmployeeRepository;
import com.example.employee.task.tracker.service.department.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,DepartmentService departmentService) {
        this.employeeRepository = employeeRepository;
        this.departmentService = departmentService;
    }

 

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                ()->new  CustomException("employee.not_found") );
    }

    @Override
    public void deleteById(Long id) {
         this.employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee save(Employee employee) {
        if(employee.getStartDate()==null)
            employee.setStartDate(LocalDate.now());
        return employeeRepository.save(employee);
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
        return employeeRepository.save(find);
    }

    @Override
    public EmployeeDto mapToDto(Employee employee) {
        EmployeeDto employeeDto=new EmployeeDto();
        employeeDto.setName(employee.getName());
        employeeDto.setFamily(employee.getFamily());
        employeeDto.setEmployeeNumber(employee.getEmployeeNumber());
        employeeDto.setAddress(employee.getAddress());
        employeeDto.setFullName(employee.getFullName());
        employeeDto.setStartDate(employee.getStartDate());
        employeeDto.setEndDate(employee.getEndDate());
        Department department = departmentService.getEmployeeCurrentDepartment(
                employee.getEmployeeNumber());
        employeeDto.setCurrentDepartment(departmentService.mapToDto(department));
        if(employee.getRole()!=null)
            employeeDto.setRole(employee.getRole().name());
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
        return employee;
    }

    @Override
    public Employee mapToDto(SignupRQ signupRQ) {
        Employee employee = new Employee();
        employee.setEmployeeNumber(signupRQ.getEmployeeNumber());
        employee.setName(signupRQ.getName());
        employee.setFamily(signupRQ.getFamily());
        employee.setAddress(signupRQ.getAddress());
        employee.setEmail(signupRQ.getEmail());
        employee.setRole(Employee.Role.EMPLOYEE);
        employee.setStartDate(LocalDate.now());
        employee.setStatus(BaseEntity.Status.ACTIVE);
        employee.setPhoneNumber(signupRQ.getPhone_number());
        Department department=departmentService.findByDepartmentCode(signupRQ.getDepartmentCode());
        DepartmentEmployees departmentEmployees =new DepartmentEmployees();
        departmentEmployees.setDepartment(department);
        departmentEmployees.setFromDate(LocalDateTime.now());
        employee.setDepartmentEmployees(List.of(departmentEmployees));
        departmentEmployees.setEmployee(employee);
        return employee;
    }

    @Override
    @StatusFilter(status = BaseEntity.Status.ACTIVE)
    public Optional<Employee> findByEmployeeNumber(String employeeNumber) {
        return  this.employeeRepository.findByEmployeeNumber(employeeNumber);

    }
}
