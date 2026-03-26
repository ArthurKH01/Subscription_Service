package com.example.test_task.subscriptionService.service.retryable;

import com.example.test_task.subscriptionService.mapper.InvoiceMapper;
import com.example.test_task.subscriptionService.model.dto.InvoiceMessageDto;
import com.example.test_task.subscriptionService.model.entity.InvoiceInfo;
import com.example.test_task.subscriptionService.model.entity.RetryableTask;
import com.example.test_task.subscriptionService.mapper.RetryableTaskMapper;
import com.example.test_task.subscriptionService.service.RabbitMQService;
import com.example.test_task.subscriptionService.service.RetryableTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendInvoiceRetryableTaskProcessor implements RetryableTaskProcessor {
    private final RetryableTaskMapper retryableTaskMapper;
    private final RetryableTaskService retryableTaskService;
    private final RabbitMQService rabbitMQService;
    private final InvoiceMapper invoiceMapper;

    @Override
    public void processRetryableTasks(List<RetryableTask> retryableTasks) {
        List<RetryableTask> successRetryableTasks = new ArrayList<>();
        for (RetryableTask retryableTask : retryableTasks) {
            try {
                InvoiceInfo invoice = retryableTaskMapper.convertJsonToInvoices(retryableTask.getPayload());
                InvoiceMessageDto messageDto = invoiceMapper.toDto(invoice);
                rabbitMQService.sendInvoice(messageDto);
                successRetryableTasks.add(retryableTask);
            } catch (Exception e) {
                log.error("Повторная попытка отправить счёт id={} завершилась неудачей: {}", retryableTask.getId(), e.getMessage(), e);
            }
        }
        if (!successRetryableTasks.isEmpty()) {
            retryableTaskService.markRetryableTasksAsCompleted(successRetryableTasks);
        }
    }
}
