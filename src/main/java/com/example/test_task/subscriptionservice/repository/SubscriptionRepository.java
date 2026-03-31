package com.example.test_task.subscriptionservice.repository;

import com.example.test_task.subscriptionservice.model.entity.Subscription;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByUserIdAndStatus(Long userId, SubscriptionStatus status);

    Optional<Subscription> findByUserIdAndTypeAndStatus(Long userId, SubscriptionType type, SubscriptionStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Subscription  s WHERE s.status = :status AND s.nextInvoiceDate <= :today")
    List<Subscription> findByStatusAndNextInvoiceDateLessThanEqual(@Param("status") SubscriptionStatus status,
                                                                   @Param("today") LocalDate today);
}
