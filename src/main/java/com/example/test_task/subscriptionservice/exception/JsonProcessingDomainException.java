package com.example.test_task.subscriptionservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JsonProcessingDomainException extends RuntimeException {
    public JsonProcessingDomainException(String message, JsonProcessingException e) {
        super(message);
    }
}
