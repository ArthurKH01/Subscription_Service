package com.example.test_task.subscriptionService.service;

import com.example.test_task.subscriptionService.mapper.InvoiceMapper;
import com.example.test_task.subscriptionService.mapper.RetryableTaskMapper;
import com.example.test_task.subscriptionService.model.dto.InvoiceMessageDto;
import com.example.test_task.subscriptionService.model.entity.InvoiceInfo;
import com.example.test_task.subscriptionService.model.entity.RetryableTask;
import com.example.test_task.subscriptionService.service.retryable.SendInvoiceRetryableTaskProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SendInvoiceRetryableTaskProcessorTest {
    RetryableTaskMapper taskMapper = mock(RetryableTaskMapper.class);
    RetryableTaskService taskService = mock(RetryableTaskService.class);
    RabbitMQService mq = mock(RabbitMQService.class);
    InvoiceMapper invoiceMapper = mock(InvoiceMapper.class);
    SendInvoiceRetryableTaskProcessor processor = new SendInvoiceRetryableTaskProcessor(taskMapper, taskService, mq, invoiceMapper);

    @Test
    void processRetryableTasks_allSuccess() {
        RetryableTask task = new RetryableTask();
        InvoiceInfo info = new InvoiceInfo();
        when(taskMapper.convertJsonToInvoices(any())).thenReturn(info);
        when(invoiceMapper.toDto(info)).thenReturn(new InvoiceMessageDto());

        processor.processRetryableTasks(List.of(task));

        verify(mq).sendInvoice(any());
        verify(taskService).markRetryableTasksAsCompleted(List.of(task));
    }

    @Test
    void processRetryableTasks_someFail() {
        RetryableTask task = new RetryableTask();
        InvoiceInfo info = new InvoiceInfo();
        when(taskMapper.convertJsonToInvoices(any())).thenReturn(info);
        when(invoiceMapper.toDto(info)).thenReturn(new InvoiceMessageDto());
        doThrow(new RuntimeException()).when(mq).sendInvoice(any());

        processor.processRetryableTasks(List.of(task));

        verify(taskService, never()).markRetryableTasksAsCompleted(any());
    }
}
