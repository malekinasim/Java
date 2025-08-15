package com.auth.service.service.task;

import com.example.employee.task.tracker.model.Task;
import com.example.employee.task.tracker.model.dto.TaskDto;
import com.example.employee.task.tracker.service.BaseService;

public interface TaskService extends BaseService<Task, Long> {
    public Task mapToEntity(TaskDto taskDto);
    public TaskDto mapToDto(Task task);
}
