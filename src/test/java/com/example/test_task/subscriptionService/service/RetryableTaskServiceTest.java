package com.example.test_task.subscriptionService.service;

import com.example.test_task.subscriptionService.mapper.RetryableTaskMapper;
import com.example.test_task.subscriptionService.model.entity.InvoiceInfo;
import com.example.test_task.subscriptionService.model.entity.RetryableTask;
import com.example.test_task.subscriptionService.model.enums.retryableTask.RetryableTaskType;
import com.example.test_task.subscriptionService.repository.RetryableTaskRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class RetryableTaskServiceTest {
    RetryableTaskRepository repo = mock(RetryableTaskRepository.class);
    RetryableTaskMapper mapper = mock(RetryableTaskMapper.class);
    RetryableTaskService service = new RetryableTaskService(repo, mapper);

    @Test
    void createRetryableTask_SavesMappedEntity() {
        InvoiceInfo inv = new InvoiceInfo();
        RetryableTask rt = new RetryableTask();
        when(mapper.toRetryableTask(inv, RetryableTaskType.SEND_INVOICE)).thenReturn(rt);

        service.createRetryableTask(inv, RetryableTaskType.SEND_INVOICE);
        verify(repo).save(rt);
    }

    @Test
    void createRetryableTasks_SavesAll() {
        InvoiceInfo inv = new InvoiceInfo();
        RetryableTask rt = new RetryableTask();
        when(mapper.toRetryableTask(inv, RetryableTaskType.SEND_INVOICE)).thenReturn(rt);

        service.createRetryableTasks(List.of(inv), RetryableTaskType.SEND_INVOICE);
        verify(repo).saveAll(anyList());
    }

    @Test
    void getRetryableTasksForProcessing_DelegatesToRepo() {
        service.limit = 10;
        service.timeoutInSeconds = 1;
        when(repo.findRetryableTaskForProcessing(any(), any(), any(), any())).thenReturn(List.of(new RetryableTask()));

        service.getRetryableTasksForProcessing(RetryableTaskType.SEND_INVOICE);
        verify(repo).findRetryableTaskForProcessing(any(), any(), any(), any());
    }

    @Test
    void markRetryableTasksAsCompleted_SavesAll() {
        RetryableTask t = new RetryableTask();
        service.markRetryableTasksAsCompleted(List.of(t));
        verify(repo).saveAll(anyList());
    }
}
