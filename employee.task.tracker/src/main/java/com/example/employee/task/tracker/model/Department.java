package com.example.employee.task.tracker.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
@Entity
@Table(name = "department")
public class Department extends OrganBaseEntity<Long> {
    @Column(name = "code", unique = true, nullable = false)
    private String departmentCode;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne(targetEntity = Employee.class)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    // A department can have a higher (parent) department
    @ManyToOne
    @JoinColumn(name = "parent_dep_id")
    private Department higherDepartment;

    @OneToMany(mappedBy = "department",targetEntity = DepartmentEmployees.class)
    Set<DepartmentEmployees>    departmentEmployeesHistories;

    @OneToMany(mappedBy = "higherDepartment", targetEntity = Department.class)
    private Set<Department> subDepartment;



    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Department getHigherDepartment() {
        return higherDepartment;
    }

    public void setHigherDepartment(Department higherDepartment) {
        this.higherDepartment = higherDepartment;
    }

    public Set<Department> getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(Set<Department> subDepartment) {
        this.subDepartment = subDepartment;
    }

    public Set<DepartmentEmployees> getDepartmentEmployeesHistories() {
        return departmentEmployeesHistories;
    }

    public void setDepartmentEmployeesHistories(Set<DepartmentEmployees> departmentEmployeesHistories) {
        this.departmentEmployeesHistories = departmentEmployeesHistories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(departmentCode, that.departmentCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(departmentCode);
    }

    @Override
    public String toString() {
        return higherDepartment != null ? "Department{" +
                "departmentCode='" + departmentCode + '\'' +
                ", name='" + name + '\'' +
                ", manager=" + manager +
                ", higherDepartment=" + higherDepartment.getName() +
                '}' :
                "Department{" +
                        "departmentCode='" + departmentCode + '\'' +
                        ", name='" + name + '\'' +
                        ", manager=" + manager +
                        '}';
    }
}
