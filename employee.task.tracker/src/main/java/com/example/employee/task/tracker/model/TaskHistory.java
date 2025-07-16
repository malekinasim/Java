package com.example.employee.task.tracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "task_historty")
public class TaskHistory extends BaseEntity<Long> {
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private StateType state;

    @ManyToOne(targetEntity = Task.class)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public StateType getState() {
        return state;
    }

    public void setState(StateType stateType) {
        this.state = stateType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public enum StateType {
        TO_DO, DOING, TEST, DONE
    }
}
