package com.example.employee.task.tracker.repoeitory.task;
import com.example.employee.task.tracker.model.Task;
import com.example.employee.task.tracker.repoeitory.BaseRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TaskRepository extends BaseRepository<Task, Long> {
}
