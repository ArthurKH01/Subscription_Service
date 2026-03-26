package com.example.test_task.subscriptionService.service.retryable;

import com.example.test_task.subscriptionService.model.entity.RetryableTask;

import java.util.List;

public interface RetryableTaskProcessor {
    void processRetryableTasks(List<RetryableTask> retryableTasks);
}
