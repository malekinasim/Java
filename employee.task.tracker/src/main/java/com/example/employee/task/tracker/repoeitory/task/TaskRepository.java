package com.example.employee.task.tracker.repoeitory.task;

import com.example.employee.task.tracker.model.BaseEntity;
import com.example.employee.task.tracker.repoeitory.BaseRepository;
import com.example.employee.task.tracker.model.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository<Task,Long > extends BaseRepository<Task,Long> {
}
