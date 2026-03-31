package com.example.test_task.subscriptionservice.model.enums.retryableTask;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RetryableTaskStatus {
    IN_PROGRESS("IN PROGRESS"),
    SUCCESS("SUCCESS");
    private final String value;
}
