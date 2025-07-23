package com.example.employee.task.tracker.controller;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.model.dto.EmployeeDto;
import com.example.employee.task.tracker.service.employee.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee/{id}")
    ResponseEntity<?> one(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/employee/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        employeeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/employee")
    ResponseEntity<?> addOne(@RequestBody @Validated EmployeeDto employeeDto) {
        Employee employee = employeeService.save(employeeService.mapToEntity(employeeDto));
        return new ResponseEntity<>(employeeService.mapToDto(employee), HttpStatus.OK);
    }

    @PutMapping("/employee")
    ResponseEntity<?> updateOne(@RequestBody @Validated EmployeeDto employeeDto) {
        Employee employee = employeeService.update(employeeService.mapToEntity(employeeDto));
        return new ResponseEntity<>(employeeService.mapToDto(employee), HttpStatus.OK);
    }

    @PostMapping("/employee/search")
    ResponseEntity<?> Search(@RequestBody EmployeeDto employeeDto) {
        Employee employee = employeeService.save(employeeService.mapToEntity(employeeDto));
        return new ResponseEntity<>(employeeService.mapToDto(employee), HttpStatus.OK);
    }

}
