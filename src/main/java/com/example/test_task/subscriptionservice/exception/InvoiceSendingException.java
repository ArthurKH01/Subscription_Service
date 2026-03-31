package com.example.test_task.subscriptionservice.exception;

import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class InvoiceSendingException extends RuntimeException {
    public InvoiceSendingException(String message, AmqpException e) {
        super(message);
    }
}
