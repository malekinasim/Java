package com.example.employee.task.tracker.model;


import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import java.util.List;
@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "family", nullable = false)
    private String family;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "employee_number", nullable = false, unique = true)
    private String employeeNumber;

    @Formula("name || ' ' || family")
    private String fullName;

    // One employee can be responsible for many tasks
    @OneToMany(mappedBy = "responsible", fetch = FetchType.LAZY)
    private List<Task> tasks;

    // --- Getters & Setters ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getFullName() { return fullName; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
