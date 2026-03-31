package com.example.test_task.subscriptionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubscriptionAlreadyActiveException extends RuntimeException {
    public SubscriptionAlreadyActiveException(String message) {
        super(message);
    }
}
