package com.example.test_task.subscriptionservice.service.activation;

import com.example.test_task.subscriptionservice.model.entity.Subscription;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionType;
import com.example.test_task.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionFactory {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription createNewSubscription(Long userId, SubscriptionType type, LocalDate activationDate) {
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setType(type);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setActivationDate(activationDate);
        subscription.setNextInvoiceDate(activationDate);

        Subscription saved = subscriptionRepository.save(subscription);

        log.info("Создана новая подписка id={} для пользователя {} типа {}", saved.getId(), userId, type);

        return saved;
    }
}

