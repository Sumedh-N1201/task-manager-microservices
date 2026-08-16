package com.sumedh.taskservice.dto;

import com.sumedh.taskservice.enums.TaskStatusEnum;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    @Size(max = 50, message = "Max title can be of 50 characters")
    private String title;

    private String description;
    private TaskStatusEnum status;
}