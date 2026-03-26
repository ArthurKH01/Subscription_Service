package com.example.test_task.subscriptionService.scheduler;

import com.example.test_task.subscriptionService.model.entity.Subscription;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionService.repository.SubscriptionRepository;
import com.example.test_task.subscriptionService.service.InvoicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceScheduler {
    private final SubscriptionRepository subscriptionRepository;
    private final InvoicesService invoicesService;

    @Scheduled(cron = "0 0 */6 * * *")
    public void createMonthlyInvoice() {
        LocalDate today = LocalDate.now();
        List<Subscription> activeSubscriptions = subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);
        for (Subscription subscription : activeSubscriptions) {
            if (subscription.getActivationDate() == null) {
                continue;
            }
            if (today.isBefore(subscription.getActivationDate())) {
                continue;
            }
            if (today.equals(subscription.getActivationDate())
                    || (today.getDayOfMonth() == subscription.getActivationDate().getDayOfMonth()
                    && today.isAfter(subscription.getActivationDate()))) {
                invoicesService.createInvoice(subscription, today);
            }
        }
    }

}
