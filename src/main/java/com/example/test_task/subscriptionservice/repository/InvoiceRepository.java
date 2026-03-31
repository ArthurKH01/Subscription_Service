package com.example.test_task.subscriptionservice.repository;

import com.example.test_task.subscriptionservice.model.entity.Invoice;
import com.example.test_task.subscriptionservice.model.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsBySubscriptionAndInvoiceDate(Subscription subscription, LocalDate invoiceDate);
    Page<Invoice> findByUserId(Long userId, Pageable pageable);
}
