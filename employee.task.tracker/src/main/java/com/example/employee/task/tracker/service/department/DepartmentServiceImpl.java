package com.example.employee.task.tracker.service.department;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.Department;
import com.example.employee.task.tracker.model.dto.DepartmentDto;
import com.example.employee.task.tracker.repoeitory.department.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;


    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentRepository getRepository() {
        return departmentRepository;
    }

    @Override
    public Department findById(Long id) {
        return getRepository().findById(id).orElseThrow(
                () -> new CustomException("department.not_found"));
    }

    @Override
    public void deleteById(Long id) {
        this.getRepository().deleteById(id);
    }

    @Override
    public List<Department> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Department save(Department department) {
        return getRepository().save(department);
    }

    @Override
    public Department update(Department department) {
        Department find = this.findById(department.getId());
        find.setName(department.getName());
        if (find.getManager() != null) {
            if (department.getManager() != null)
                if (!find.getManager().getId().equals(department.getManager().getId()))
                    find.setManager(department.getManager());
                else
                    find.setManager(null);
        }
        if (!CollectionUtils.isEmpty(department.getSubDepartment()))
            find.setSubDepartment(department.getSubDepartment());
        return getRepository().save(find);
    }

    @Override
    public Department getEmployeeCurrentDepartment(String employeeNumber) {
        return getRepository().getEmployeeCurrentDepartment(employeeNumber).orElseThrow(()->
                new CustomException("employee.current.department.not_found"));
    }

    @Override
    public DepartmentDto mapToDto(Department department) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setName(department.getName());
        departmentDto.setDepartmentCode(department.getDepartmentCode());
        if (!CollectionUtils.isEmpty(department.getSubDepartment()))
            departmentDto.setSubDepartment(department.getSubDepartment().stream().map(this::mapToDto).collect(Collectors.toSet()));
        return departmentDto;
    }

    @Override
    public Department mapToEntity(DepartmentDto departmentDto) {
        return null;
    }
}
