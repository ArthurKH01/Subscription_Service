package com.example.test_task.subscriptionservice.service.retryable;

import com.example.test_task.subscriptionservice.model.entity.RetryableTask;

import java.util.List;

public interface RetryableTaskProcessor {
    void processRetryableTasks(List<RetryableTask> retryableTasks);
}
