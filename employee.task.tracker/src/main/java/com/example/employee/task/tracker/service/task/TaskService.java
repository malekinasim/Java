package com.example.employee.task.tracker.service.task;
import com.example.employee.task.tracker.model.Task;
import com.example.employee.task.tracker.model.dto.TaskDto;
import com.example.employee.task.tracker.repoeitory.task.TaskRepository;
import com.example.employee.task.tracker.service.BaseService;

public interface TaskService extends BaseService<Task, Long, TaskRepository> {
    public Task mapToEntity(TaskDto taskDto);
    public TaskDto mapToDto(Task task);
}
