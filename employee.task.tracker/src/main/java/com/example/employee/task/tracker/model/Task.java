package com.example.employee.task.tracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
@Entity
@Table(name = "tasks")
public class Task extends OrganBaseEntity<Long> {

    @Column(name = "task_number", nullable = false, unique = true)
    private String taskNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "id",targetEntity = TaskHistory.class, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<TaskHistory> taskHistorySet;

    // Many tasks can belong to one employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)  // FK in tasks table
    private Employee responsible;

    // --- Getters & Setters ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Employee getResponsible() {
        return responsible;
    }

    public void setResponsible(Employee responsible) {
        this.responsible = responsible;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public Set<TaskHistory> getTaskHistorySet() {
        return taskHistorySet;
    }

    public void setTaskHistorySet(Set<TaskHistory> taskHistorySet) {
        this.taskHistorySet = taskHistorySet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskNumber, task.taskNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskNumber);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskNumber='" + taskNumber + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", responsible=" + responsible +
                '}';
    }


}
