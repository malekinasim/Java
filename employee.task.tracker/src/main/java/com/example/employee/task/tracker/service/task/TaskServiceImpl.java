package com.example.employee.task.tracker.service.task;

import com.example.employee.task.tracker.model.Employee;
import com.example.employee.task.tracker.model.Task;
import com.example.employee.task.tracker.repoeitory.task.TaskRepository;
import com.example.employee.task.tracker.service.employee.EmployeeService;
import org.springframework.stereotype.Service;
import com.example.employee.task.tracker.model.dto.TaskDto;
import java.time.LocalDate;
import java.util.List;
import com.example.employee.task.tracker.config.exception.CustomException;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeService employeeService;
    public TaskServiceImpl(TaskRepository taskRepository, EmployeeService employeeService) {
        this.taskRepository = taskRepository;
        this.employeeService = employeeService;
    }

    @Override
    public TaskRepository<Task, Long> getRepository() {
        return taskRepository;
    }

    @Override
    public Task findById(Long id) {
        return getRepository().findById(id).orElseThrow(()->
                new CustomException("task.not_found",id));
    }

    @Override
    public void deleteById(Long id) {
        this.findById(id);
         getRepository().deleteById(id);
    }

    @Override
    public List<Task> findAll() {
        return getRepository().findAll();
    }

    @Override
    public TaskDto mapToDto(Task task) {
        TaskDto taskDto=new TaskDto();
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setStartDate(task.getStartDate());
        taskDto.setEndDate(task.getEndDate());
        taskDto.setStatus(task.getStatus().name());

        return taskDto;
    }

    public Task mapToEntity(TaskDto taskDto){
        Task task=new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStartDate(taskDto.getStartDate());
        task.setEndDate(taskDto.getEndDate());
        if(taskDto.getStatus()!=null) {
            task.setStatus(Task.StatusType.valueOf(taskDto.getStatus()));
        } else{
            if(task.getStartDate().isAfter(LocalDate.now()))
               task.setStatus(Task.StatusType.TO_DO);
            else if(task.getStartDate().isBefore(LocalDate.now())  ) {
                if (task.getEndDate() == null || task.getEndDate().isAfter(LocalDate.now()))
                    task.setStatus(Task.StatusType.DOING);
                else
                    task.setStatus(Task.StatusType.DONE);
            }
        }
        if(taskDto.getResponsible_id()!=null) {
           Employee employee= employeeService.findById(taskDto.getResponsible_id());
            task.setResponsible(employee);
        }

        return task;
    }

    @Override
    public Task save(Task task) {
        if(task.getStartDate()==null)
           task.setStartDate(LocalDate.now());
        if(task.getStatus()==null )
           task.setStatus(Task.StatusType.TO_DO);
        return getRepository().save(task);
    }

    @Override
    public Task update(Task task) {
        Task find=this.findById(task.getId());
        find.setName(task.getName());
        find.setDescription(task.getDescription());
        find.setStartDate(task.getStartDate());
        find.setEndDate(task.getEndDate());
        find.setResponsible(task.getResponsible());
        return getRepository().save(find);
    }
}
