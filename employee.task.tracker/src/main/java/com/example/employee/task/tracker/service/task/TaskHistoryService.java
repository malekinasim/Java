package com.example.employee.task.tracker.service.task;

import com.example.employee.task.tracker.model.TaskHistory;
import com.example.employee.task.tracker.repoeitory.task.TaskHistoryRepository;
import com.example.employee.task.tracker.service.BaseService;
public interface TaskHistoryService extends BaseService<TaskHistory, Long, TaskHistoryRepository> {

    TaskHistory getLastTaskStatus(Long taskId);
}
