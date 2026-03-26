package com.example.test_task.subscriptionService.repository;

import com.example.test_task.subscriptionService.model.entity.InvoiceInfo;
import com.example.test_task.subscriptionService.model.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InvoicesRepository extends JpaRepository<InvoiceInfo, Long> {
    boolean existsBySubscriptionAndInvoiceDate(Subscription subscription, LocalDate invoiceDate);
    Page<InvoiceInfo> findByUserIdOrderByInvoiceDateDesc(Long userId, Pageable pageable);
}
