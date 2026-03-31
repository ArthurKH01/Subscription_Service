package com.example.test_task.subscriptionservice.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceScheduler {
    private final SubscriptionInvoiceProcessor processor;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void createMonthlyInvoice() {
        log.info("Запуск InvoiceScheduler для выставления ежемесячных счётов");
        LocalDate today = LocalDate.now();
        processor.processInvoiceForDate(today);
        log.info("Завершение работы InvoiceScheduler");
    }
}
