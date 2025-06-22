package com.example.employee.task.tracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private StatusType status;

  // Many tasks can belong to one employee
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)  // FK in tasks table
  private Employee responsible;

  // --- Getters & Setters ---
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public LocalDate getStartDate() { return startDate; }
  public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

  public LocalDate getEndDate() { return endDate; }
  public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

  public StatusType getStatus() { return status; }
  public void setStatus(StatusType status) { this.status = status; }

  public Employee getResponsible() { return responsible; }
  public void setResponsible(Employee responsible) { this.responsible = responsible; }

  public enum StatusType {
    TO_DO, DOING, TEST, DONE
  }
}
