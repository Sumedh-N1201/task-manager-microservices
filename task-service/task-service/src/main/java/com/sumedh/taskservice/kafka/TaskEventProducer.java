package com.sumedh.taskservice.kafka;

import com.sumedh.taskservice.entity.Task;
import com.sumedh.taskservice.event.TaskEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskEventProducer {

    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;

    public TaskEventProducer(KafkaTemplate<String, TaskEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTaskCreatedEvent(String msg, Task task) {
        TaskEvent event = new TaskEvent("TASK_CREATED", task.getId(),
                task.getTitle(),
                task.getStatus(),
                LocalDateTime.now());
        kafkaTemplate.send("task-created-topic", "Task created with ID : ", event);
    }
}