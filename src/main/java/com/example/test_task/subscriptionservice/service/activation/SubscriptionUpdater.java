package com.example.test_task.subscriptionservice.service.activation;

import com.example.test_task.subscriptionservice.model.entity.Subscription;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class SubscriptionUpdater {
    public void update(Subscription subscription, LocalDate activationDate) {
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setActivationDate(activationDate);
        subscription.setNextInvoiceDate(activationDate);

        log.info("Реактивирована подписка id={} для пользователя {}", subscription.getId(), subscription.getUserId());
    }
}
