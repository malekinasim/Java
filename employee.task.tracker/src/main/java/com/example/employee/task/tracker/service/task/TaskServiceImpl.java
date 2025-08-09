package com.example.employee.task.tracker.service.task;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.model.Task;
import com.example.employee.task.tracker.model.TaskHistory;
import com.example.employee.task.tracker.model.dto.TaskDto;
import com.example.employee.task.tracker.repository.task.TaskRepository;
import com.example.employee.task.tracker.service.employee.EmployeeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class TaskServiceImpl implements com.example.employee.task.tracker.service.task.TaskService {
    private final TaskRepository taskRepository;
    @Lazy
    private final EmployeeService employeeService;
    private final TaskHistoryService taskHistoryService;

    public TaskServiceImpl(TaskRepository taskRepository, EmployeeService employeeService, TaskHistoryService taskHistoryService) {
        this.taskRepository = taskRepository;
        this.employeeService = employeeService;
        this.taskHistoryService = taskHistoryService;
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new CustomException("task.not_found", id));
    }

    @Override
    public void deleteById(Long id) {
        this.findById(id);
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public TaskDto mapToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setStartDate(task.getStartDate());
        taskDto.setEndDate(task.getEndDate());
        try {

            TaskHistory taskHistory = taskHistoryService.getLastTaskStatus(task.getId());
            taskDto.setStatus(taskHistory.getState().name());
        } catch (CustomException e) {
            e.printStackTrace();
        }
        taskDto.setTaskNumber(task.getTaskNumber());
        if(task.getResponsible()!=null)
          taskDto.setEmployeeDto(employeeService.mapToDto(task.getResponsible()));
        return taskDto;
    }
    @Override
    public Task mapToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStartDate(taskDto.getStartDate());
        task.setEndDate(taskDto.getEndDate());


        try {

            TaskHistory taskHistory = taskHistoryService.getLastTaskStatus(task.getId());
            taskDto.setStatus(taskHistory.getState().name());
        } catch (CustomException e) {
            e.printStackTrace();
        }
        TaskHistory taskHistory = new TaskHistory();
        if (taskDto.getStatus() != null) {
            taskHistory.setState(TaskHistory.StateType.valueOf(taskDto.getStatus()));

        } else {
            if (task.getStartDate().isAfter(LocalDate.now()))
                taskHistory.setState(TaskHistory.StateType.TO_DO);
            else if (task.getStartDate().isBefore(LocalDate.now())) {
                if (task.getEndDate() == null || task.getEndDate().isAfter(LocalDate.now()))
                    taskHistory.setState(TaskHistory.StateType.DOING);
                else
                    taskHistory.setState(TaskHistory.StateType.DONE);
            }
        }

        task.setTaskHistorySet(Set.of(taskHistory));
        if (taskDto.getResponsible_id() != null) {
            Employee employee = employeeService.findById(taskDto.getResponsible_id());
            task.setResponsible(employee);
        }
        task.setTaskNumber(taskDto.getTaskNumber());
        return task;
    }
    @Override
    public Task save(Task task) {
        if (task.getStartDate() == null)
            task.setStartDate(LocalDate.now());
        if (CollectionUtils.isEmpty(task.getTaskHistorySet())) {
            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setFromDate(LocalDate.now());
            taskHistory.setState(TaskHistory.StateType.TO_DO);
            taskHistory.setTask(task);
            task.setTaskHistorySet(Set.of(taskHistory));
        }
        return taskRepository.save(task);
    }

    @Override
    public Task update(Task task) {
        Task find = this.findById(task.getId());
        find.setName(task.getName());
        find.setDescription(task.getDescription());
        find.setStartDate(task.getStartDate());
        find.setEndDate(task.getEndDate());
        find.setResponsible(task.getResponsible());
        find.setTaskNumber(task.getTaskNumber());
        find.setResponsible(task.getResponsible());
        return taskRepository.save(find);
    }
}
