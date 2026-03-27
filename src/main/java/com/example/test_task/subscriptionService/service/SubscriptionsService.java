package com.example.test_task.subscriptionService.service;

import com.example.test_task.subscriptionService.model.dto.ActivateRequest;
import com.example.test_task.subscriptionService.model.dto.DeactivateRequest;
import com.example.test_task.subscriptionService.model.entity.Subscriptions;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionType;
import com.example.test_task.subscriptionService.repository.SubscriptionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionsService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public void activate(ActivateRequest activateRequest) {
        Long userId = activateRequest.getUserId();
        SubscriptionType type = activateRequest.getSubscriptionType();
        LocalDate activationDate = activateRequest.getActivationDate();

        boolean isActive = subscriptionRepository.existsByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        if (isActive) {
            throw new IllegalArgumentException("Пользователь не может иметь несколько активных подписок!");
        }
        Optional<Subscriptions> inactiveSubscription = subscriptionRepository.findByUserIdAndTypeAndStatus(userId, type, SubscriptionStatus.INACTIVE);
        if (inactiveSubscription.isPresent()) {
            Subscriptions subscription = inactiveSubscription.get();
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setActivationDate(activationDate);
            subscription.setNextInvoiceDate(activationDate);
            log.info("Пользователь {} реактивировал подписку {} с типом {} и датой активации {}", userId, subscription.getId(), type, activationDate);
        } else {
            Subscriptions subscription = new Subscriptions();
            subscription.setUserId(userId);
            subscription.setType(type);
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setActivationDate(activationDate);
            subscription.setNextInvoiceDate(activationDate);

            subscriptionRepository.save(subscription);
            log.info("Активирована подписка для пользователя {} типа {} с датой активации {}", userId, type, activationDate);
        }
    }


    @Transactional
    public void deactivate(DeactivateRequest deactivateRequest) {
        Long userId = deactivateRequest.getUserId();
        SubscriptionType type = deactivateRequest.getSubscriptionType();

        Subscriptions subscription = subscriptionRepository.findByUserIdAndTypeAndStatus(userId, type, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не имеет активную подписку!"));
        subscription.setStatus(SubscriptionStatus.INACTIVE);

        log.info("Деактивирована подписка для пользователя {} типа {}", userId, type);
    }
}
