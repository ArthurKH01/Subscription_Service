package com.example.test_task.subscriptionService.repository;

import com.example.test_task.subscriptionService.model.entity.Subscription;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByUserIdAndStatus(Long userId, SubscriptionStatus status);

    Optional<Subscription> findByUserIdAndTypeAndStatus(Long userId, SubscriptionType type, SubscriptionStatus status);

    List<Subscription> findByStatusAndNextInvoiceDateLessThanEqual(SubscriptionStatus status, LocalDate today);

    List<Subscription> findByUserId(Long userId);
}
