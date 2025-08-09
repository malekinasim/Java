package com.example.employee.task.tracker.repository.task;
import com.example.employee.task.tracker.model.Task;
import com.example.employee.task.tracker.repository.BaseRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TaskRepository extends BaseRepository<Task, Long> {
}
