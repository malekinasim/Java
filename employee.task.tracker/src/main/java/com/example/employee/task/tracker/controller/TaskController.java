package com.example.employee.task.tracker.controller;

import com.example.employee.task.tracker.model.Task;
import com.example.employee.task.tracker.model.dto.TaskDto;
import com.example.employee.task.tracker.service.task.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    ResponseEntity<?> one(@PathVariable Long id) {
        Task task = taskService.findById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/add")
    ResponseEntity<?> addOne(@RequestBody @Validated TaskDto taskDto) {
        Task t = taskService.mapToEntity(taskDto);
        Task task = taskService.save(t);
        return new ResponseEntity<>(taskService.mapToDto(task), HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<?> updateOne(@RequestBody @Validated TaskDto taskDto) {
        Task task = taskService.update(taskService.mapToEntity(taskDto));
        return new ResponseEntity<>(taskService.mapToDto(task), HttpStatus.OK);
    }

    @PostMapping("/search")
    ResponseEntity<?> Search(@RequestBody TaskDto taskDto) {
        Task task = taskService.save(taskService.mapToEntity(taskDto));
        return new ResponseEntity<>(taskService.mapToDto(task), HttpStatus.OK);
    }

}