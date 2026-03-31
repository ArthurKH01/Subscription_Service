package com.example.test_task.subscriptionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ActiveSubscriptionNotFoundException extends RuntimeException {
    public ActiveSubscriptionNotFoundException(String message) {
        super(message);
    }
}
