package com.example.employee.task.tracker.controller;

import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.model.dto.DepartmentDto;
import com.example.employee.task.tracker.service.department.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @GetMapping("/Department/{id}")
    ResponseEntity<?> one(@PathVariable Long id) {
        Department department = departmentService.findById(id);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @DeleteMapping("/Department/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        departmentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/Department")
    ResponseEntity<?> addOne(@RequestBody @Validated DepartmentDto departmentDto) {
        Department t = departmentService.mapToEntity(departmentDto);
        Department Department = departmentService.save(t);
        return new ResponseEntity<>(departmentService.mapToDto(Department), HttpStatus.OK);
    }

    @PutMapping("/Department")
    ResponseEntity<?> updateOne(@RequestBody @Validated DepartmentDto departmentDto) {
        Department Department = departmentService.update(departmentService.mapToEntity(departmentDto));
        return new ResponseEntity<>(departmentService.mapToDto(Department), HttpStatus.OK);
    }

    @PostMapping("/Department/search")
    ResponseEntity<?> Search(@RequestBody DepartmentDto departmentDto) {
        Department Department = departmentService.save(departmentService.mapToEntity(departmentDto));
        return new ResponseEntity<>(departmentService.mapToDto(Department), HttpStatus.OK);
    }
}
