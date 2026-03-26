package com.example.test_task.subscriptionService.mapper;

import com.example.test_task.subscriptionService.model.entity.InvoiceInfo;
import com.example.test_task.subscriptionService.model.entity.RetryableTask;
import com.example.test_task.subscriptionService.model.enums.retryableTask.RetryableTaskStatus;
import com.example.test_task.subscriptionService.model.enums.retryableTask.RetryableTaskType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@AllArgsConstructor
public class RetryableTaskMapper {
    private final ObjectMapper objectMapper;

    public RetryableTask toRetryableTask(InvoiceInfo invoice, RetryableTaskType retryableTaskType) {
        if (invoice == null) {
            return null;
        }
        RetryableTask retryableTask = new RetryableTask();
        retryableTask.setPayload(convertToJson(invoice));
        retryableTask.setType(retryableTaskType);
        retryableTask.setStatus(RetryableTaskStatus.IN_PROGRESS);
        retryableTask.setRetryTime(Instant.now());
        return retryableTask;
    }

    private String convertToJson(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    public InvoiceInfo convertJsonToInvoices(String json){
        if (json == null){
            return null;
        }
        try {
            return objectMapper.readValue(json, InvoiceInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to Invoices", e);
        }
    }
}
