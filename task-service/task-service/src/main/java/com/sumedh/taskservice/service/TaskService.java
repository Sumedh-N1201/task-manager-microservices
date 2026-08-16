package com.sumedh.taskservice.service;

import com.sumedh.taskservice.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO dto);

    List<TaskDTO> createTasks(List<TaskDTO> dtos);

    List<TaskDTO> getAllTasks(int page, int size);

    TaskDTO getTaskById(Long id);

    TaskDTO updateTask(Long id, TaskDTO dto);

    void deleteTask(Long id);
}