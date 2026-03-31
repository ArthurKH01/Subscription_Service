package com.example.test_task.subscriptionservice.service;

import com.example.test_task.subscriptionservice.exception.ActiveSubscriptionNotFoundException;
import com.example.test_task.subscriptionservice.exception.SubscriptionAlreadyActiveException;
import com.example.test_task.subscriptionservice.model.dto.ActivateRequest;
import com.example.test_task.subscriptionservice.model.dto.DeactivateRequest;
import com.example.test_task.subscriptionservice.model.entity.Subscription;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionType;
import com.example.test_task.subscriptionservice.repository.SubscriptionRepository;
import com.example.test_task.subscriptionservice.service.activation.SubscriptionFactory;
import com.example.test_task.subscriptionservice.service.activation.SubscriptionUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionUpdater subscriptionUpdater;
    private final SubscriptionFactory subscriptionFactory;

    @Transactional
    public void activate(ActivateRequest activateRequest) {
        Long userId = activateRequest.userId();
        SubscriptionType type = activateRequest.type();
        LocalDate activationDate = activateRequest.activationDate();

        boolean isActive = subscriptionRepository.existsByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        if (isActive) {
            throw new SubscriptionAlreadyActiveException("Пользователь не может иметь несколько активных подписок!");
        }

        Optional<Subscription> inactiveSubscription = subscriptionRepository.findByUserIdAndTypeAndStatus(userId, type, SubscriptionStatus.INACTIVE);

        if (inactiveSubscription.isPresent()) {
            Subscription subscription = inactiveSubscription.get();
            subscriptionUpdater.update(subscription, activationDate);

            log.info("Активирована существующая подписка id={} для пользователя {}", subscription.getId(), userId);

        } else {
            Subscription newSubscription = subscriptionFactory.createNewSubscription(userId, type, activationDate);

            log.info("Создана и активирована новая подписка id={}", newSubscription.getId());
        }
    }

    @Transactional
    public void deactivate(DeactivateRequest deactivateRequest) {
        Long userId = deactivateRequest.userId();
        SubscriptionType type = deactivateRequest.type();

        Subscription subscription = subscriptionRepository.findByUserIdAndTypeAndStatus(userId, type, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new ActiveSubscriptionNotFoundException("Пользователь не имеет активную подписку!"));
        subscription.setStatus(SubscriptionStatus.INACTIVE);

        log.info("Деактивирована подписка для пользователя {}", userId);
    }
}
