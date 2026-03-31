package com.example.test_task.subscriptionservice.scheduler;

import com.example.test_task.subscriptionservice.model.entity.Subscription;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionservice.repository.SubscriptionRepository;
import com.example.test_task.subscriptionservice.service.InvoicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionInvoiceProcessor {
    private final SubscriptionRepository subscriptionRepository;
    private final InvoicesService invoicesService;

    @Transactional
    public void processInvoiceForDate(LocalDate today) {
        List<Subscription> activeSubscriptions = subscriptionRepository
                .findByStatusAndNextInvoiceDateLessThanEqual(SubscriptionStatus.ACTIVE, today);
        for (Subscription subscription : activeSubscriptions) {
            invoicesService.createInvoice(subscription, today);
            subscription.setNextInvoiceDate(today.plusMonths(1));
        }
    }
}
