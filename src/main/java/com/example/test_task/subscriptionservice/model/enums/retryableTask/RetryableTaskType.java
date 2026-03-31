package com.example.test_task.subscriptionservice.model.enums.retryableTask;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RetryableTaskType {

    SEND_INVOICE("SEND INVOICE");

    private final String value;
}
