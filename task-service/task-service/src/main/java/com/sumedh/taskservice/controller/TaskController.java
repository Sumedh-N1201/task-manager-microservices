package com.sumedh.taskservice.controller;

import com.sumedh.taskservice.dto.TaskDTO;
import com.sumedh.taskservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO task) {
        log.info("Creating task {}", task.getTitle());
        TaskDTO createdTask = taskService.createTask(task);
        return ResponseEntity.status(201).body(createdTask);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<TaskDTO>> createTasks(@Valid @RequestBody List<TaskDTO> tasks) {
        log.info("Creating {} tasks", tasks.size());
        List<TaskDTO> createdTasks = taskService.createTasks(tasks);
        return ResponseEntity.status(201).body(createdTasks);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching tasks page {} size {}", page, size);
        return ResponseEntity.ok(taskService.getAllTasks(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        log.info("Fetching tasks {}", id);
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO task) {
        log.info("Updating the task {}", task.getTitle());
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        log.info("Deleted the task {}", id);
        return ResponseEntity.noContent().build();
    }
}