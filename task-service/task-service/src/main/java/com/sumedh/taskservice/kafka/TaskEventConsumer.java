package com.sumedh.taskservice.kafka;

import com.sumedh.taskservice.event.TaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskEventConsumer {

    @KafkaListener(topics = "task-created-topic", groupId = "task-group")
    public void consume(@Payload TaskEvent task) {
        log.info("Task created: id={}, title={}", task.getTaskId(), task.getTitle());
    }
}