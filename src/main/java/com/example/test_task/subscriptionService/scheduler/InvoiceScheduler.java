package com.example.test_task.subscriptionService.scheduler;

import com.example.test_task.subscriptionService.model.entity.Subscriptions;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionService.repository.SubscriptionRepository;
import com.example.test_task.subscriptionService.service.InvoicesService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceScheduler {
    private final SubscriptionRepository subscriptionRepository;
    private final InvoicesService invoicesService;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void createMonthlyInvoice() {
        LocalDate today = LocalDate.now();
        List<Subscriptions> activeSubscriptions = subscriptionRepository
                .findByStatusAndNextInvoiceDateLessThanEqual(SubscriptionStatus.ACTIVE, today);
        for (Subscriptions subscription : activeSubscriptions) {
            invoicesService.createInvoice(subscription, today);
            subscription.setNextInvoiceDate(today.plusMonths(1));
        }
    }

}
