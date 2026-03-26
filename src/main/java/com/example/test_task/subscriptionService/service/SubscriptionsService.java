package com.example.test_task.subscriptionService.service;

import com.example.test_task.subscriptionService.model.dto.ActivateRequest;
import com.example.test_task.subscriptionService.model.dto.DeactivateRequest;
import com.example.test_task.subscriptionService.model.entity.Subscription;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionType;
import com.example.test_task.subscriptionService.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

        if (activationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Дата активации не может быть в прошлом!");
        }

        boolean isActive = subscriptionRepository.existsByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        if (isActive) {
            throw new IllegalArgumentException("Пользователь не может иметь несколько активных подписок!");
        }

        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setType(type);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setActivationDate(activationDate);

        subscriptionRepository.save(subscription);

        log.info("Активирована подписка для пользователя {} типа {} с датой активации {}", userId, type, activationDate);
    }

    @Transactional
    public void deactivate(DeactivateRequest deactivateRequest) {
        Long userId = deactivateRequest.getUserId();
        SubscriptionType type = deactivateRequest.getSubscriptionType();

        Subscription subscription = subscriptionRepository.findByUserIdAndTypeAndStatus(userId, type, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не имеет активную подписку!"));
        subscription.setStatus(SubscriptionStatus.INACTIVE);

        subscriptionRepository.save(subscription);


        log.info("Деактивирована подписка для пользователя {} типа {}", userId, type);
    }
}
