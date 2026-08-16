package com.sumedh.taskservice.event;

import com.sumedh.taskservice.enums.TaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEvent {
    private String eventType;
    private long taskId;
    private String title;
    private TaskStatusEnum status;
    private LocalDateTime timestamp;
}