package com.example.test_task.subscriptionService.model.enums.retryableTask;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RetryableTaskType {

    SEND_INVOICE("SEND INVOICE");

    private final String value;

    public static RetryableTaskType fromValue(String value) {
        for (RetryableTaskType status : RetryableTaskType.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
    }
