package com.auth.service.service.task;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.TaskHistory;
import com.example.employee.task.tracker.repository.task.TaskHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskHistoryServiceImpl implements TaskHistoryService {
    private final TaskHistoryRepository taskHistoryRepository;

    public TaskHistoryServiceImpl(TaskHistoryRepository taskHistoryRepository) {
        this.taskHistoryRepository = taskHistoryRepository;
    }

   
    @Override
    public TaskHistory findById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }
    @Override
    public List<TaskHistory> findAll() {
        return List.of();
    }

    @Override
    public TaskHistory save(TaskHistory taskHistory) {
        return null;
    }

    @Override
    public TaskHistory update(TaskHistory taskHistory) {
        return null;
    }


    @Override
    public TaskHistory getLastTaskStatus(Long taskId) {
        return taskHistoryRepository.getLastTaskStatus(taskId).orElseThrow(
                () -> new CustomException("taskHistory.notFound")
        );
    }
}
