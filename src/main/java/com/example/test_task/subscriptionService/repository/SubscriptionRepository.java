package com.example.test_task.subscriptionService.repository;

import com.example.test_task.subscriptionService.model.entity.Subscriptions;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscriptions, Long> {
    boolean existsByUserIdAndStatus(Long userId, SubscriptionStatus status);

    Optional<Subscriptions> findByUserIdAndTypeAndStatus(Long userId, SubscriptionType type, SubscriptionStatus status);

    List<Subscriptions> findByStatusAndNextInvoiceDateLessThanEqual(SubscriptionStatus status, LocalDate today);

    List<Subscriptions> findByUserId(Long userId);
}
