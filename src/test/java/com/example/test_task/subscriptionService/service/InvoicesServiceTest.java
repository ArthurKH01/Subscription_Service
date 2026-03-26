package com.example.test_task.subscriptionService.service;

import com.example.test_task.subscriptionService.model.entity.Subscription;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionType;
import com.example.test_task.subscriptionService.repository.InvoicesRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InvoicesServiceTest {
    InvoicesRepository repo = mock(InvoicesRepository.class);
    RetryableTaskService retry = mock(RetryableTaskService.class);
    RabbitMQService mq = mock(RabbitMQService.class);
    com.example.test_task.subscriptionService.mapper.InvoiceMapper mapper = mock(com.example.test_task.subscriptionService.mapper.InvoiceMapper.class);

    InvoicesService service = new InvoicesService(repo, retry, mq, mapper);

    private Subscription createSubscription() {
        Subscription sub = new Subscription();
        sub.setType(SubscriptionType.BASIC);
        return sub;
    }

    @Test
    void createInvoice_NotSavesIfExists() {
        Subscription sub = createSubscription();
        when(repo.existsBySubscriptionAndInvoiceDate(any(), any())).thenReturn(true);
        service.createInvoice(sub, LocalDate.now());
        verify(repo, never()).save(any());
    }

    @Test
    void createInvoice_SavesIfNotExists() {
        Subscription sub = createSubscription();
        when(repo.existsBySubscriptionAndInvoiceDate(any(), any())).thenReturn(false);
        when(repo.save(any())).thenReturn(new com.example.test_task.subscriptionService.model.entity.InvoiceInfo());
        when(mapper.toDto(any())).thenReturn(new com.example.test_task.subscriptionService.model.dto.InvoiceMessageDto());

        service.createInvoice(sub, LocalDate.now());
        verify(repo).save(any());
        verify(mq).sendInvoice(any());
    }

    @Test
    void createInvoice_RunsRetryIfMqFails() {
        Subscription sub = createSubscription();
        when(repo.existsBySubscriptionAndInvoiceDate(any(), any())).thenReturn(false);
        when(repo.save(any())).thenReturn(new com.example.test_task.subscriptionService.model.entity.InvoiceInfo());
        when(mapper.toDto(any())).thenReturn(new com.example.test_task.subscriptionService.model.dto.InvoiceMessageDto());
        doThrow(new RuntimeException()).when(mq).sendInvoice(any());

        service.createInvoice(sub, LocalDate.now());
        verify(retry).createRetryableTask(any(), any());
    }
}
