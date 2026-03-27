package com.example.test_task.subscriptionService.repository;

import com.example.test_task.subscriptionService.model.entity.Invoices;
import com.example.test_task.subscriptionService.model.entity.Subscriptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InvoicesRepository extends JpaRepository<Invoices, Long> {
    boolean existsBySubscriptionAndInvoiceDate(Subscriptions subscription, LocalDate invoiceDate);
    Page<Invoices> findByUserIdOrderByInvoiceDateDesc(Long userId, Pageable pageable);
}
