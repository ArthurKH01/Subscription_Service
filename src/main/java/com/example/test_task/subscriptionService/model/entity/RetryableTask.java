package com.example.test_task.subscriptionService.model.entity;

import com.example.test_task.subscriptionService.model.enums.retryableTask.RetryableTaskStatus;
import com.example.test_task.subscriptionService.model.enums.retryableTask.RetryableTaskType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;

/**
 * Задачи, требующие повторного выполнения
 */
@Entity
@Table(name = "retryable_task")
@Setter
@Getter
public class RetryableTask {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Тело задачи
     */
    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String payload;
    /**
     * Тип задачи
     */
    @Enumerated(EnumType.STRING)
    private RetryableTaskType type;
    /**
     * Статус задачи
     */
    @Enumerated(EnumType.STRING)
    private RetryableTaskStatus status;
    /**
     * Время повторного выполнения
     */
    private Instant retryTime;
}
