package com.sumedh.taskservice.serviceImpl;

import com.sumedh.taskservice.dto.TaskDTO;
import com.sumedh.taskservice.entity.Task;
import com.sumedh.taskservice.exception.TaskNotFoundException;
import com.sumedh.taskservice.exception.UnauthorizedAccessException;
import com.sumedh.taskservice.kafka.TaskEventProducer;
import com.sumedh.taskservice.repository.TaskRepository;
import com.sumedh.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskEventProducer taskEventProducer;

    @Override
    @Transactional
    public TaskDTO createTask(TaskDTO dto) {
        String email = currentEmail();
        Long userId = currentUserId();

        Task task = convertToEntity(dto);
        task.setOwnerId(userId);
        task.setOwnerEmail(email);

        Task savedTask = taskRepository.save(task);
        taskEventProducer.sendTaskCreatedEvent("Task is created with ID : " + savedTask.getId(), savedTask);
        log.info("Task is created with ID : " + savedTask.getId());
        return convertToDTO(savedTask);
    }

    @Override
    @Transactional
    public List<TaskDTO> createTasks(List<TaskDTO> dtos) {
        String email = currentEmail();
        Long userId = currentUserId();

        List<Task> tasks = dtos.stream()
                .map(this::convertToEntity)
                .peek(task -> {
                    task.setOwnerId(userId);
                    task.setOwnerEmail(email);
                })
                .toList();

        List<Task> savedTasks = taskRepository.saveAll(tasks);
        savedTasks.forEach(task -> taskEventProducer.sendTaskCreatedEvent("Task is created with ID : " + task.getId(), task));
        savedTasks.forEach(task -> log.info("Task is created with ID : " + task.getId()));

        return savedTasks.stream().map(this::convertToDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks(int page, int size) {
        String email = currentEmail();
        return taskRepository.findByOwnerEmail(email, PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id : " + id));

        if (!task.getOwnerEmail().equals(currentEmail())) {
            throw new UnauthorizedAccessException("You are not authorised to view this task");
        }

        return convertToDTO(task);
    }

    @Override
    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO dto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id : " + id));

        if (!existingTask.getOwnerEmail().equals(currentEmail())) {
            throw new UnauthorizedAccessException("You are not authorised to view this task");
        }

        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setStatus(dto.getStatus());

        Task updatedTask = taskRepository.save(existingTask);
        return convertToDTO(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        try {
            taskRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new TaskNotFoundException("Task not found with ID : " + id);
        }
    }

    private String currentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Long currentUserId() {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getDetails();
    }

    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
    }

    private Task convertToEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        return task;
    }
}