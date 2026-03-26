package com.example.test_task.subscriptionService.scheduler;

import com.example.test_task.subscriptionService.model.enums.retryableTask.RetryableTaskType;
import com.example.test_task.subscriptionService.service.RetryableTaskService;
import com.example.test_task.subscriptionService.service.retryable.RetryableTaskProcessor;
import com.example.test_task.subscriptionService.service.retryable.SendInvoiceRetryableTaskProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryableTaskScheduler {
    private final RetryableTaskService retryableTaskService;
    private final SendInvoiceRetryableTaskProcessor sendInvoiceRetryableTaskProcessor;

    @Scheduled(fixedRate = 5000)
    public void executeRetryableTasks() {
        log.info("Starting retryable task processor");
        Map<RetryableTaskType, RetryableTaskProcessor> taskProcessor = Map.of(
                RetryableTaskType.SEND_INVOICE, sendInvoiceRetryableTaskProcessor
        );
        for (Map.Entry<RetryableTaskType, RetryableTaskProcessor> entry : taskProcessor.entrySet()){
            var taskType = entry.getKey();
            var processor = entry.getValue();

            var retryableTasks  = retryableTaskService.getRetryableTasksForProcessing(taskType);
            if (retryableTasks == null || retryableTasks.isEmpty()) {
                continue;
            }
            processor.processRetryableTasks(retryableTasks);
        }
    }

}
