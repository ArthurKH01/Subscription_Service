package com.example.test_task.subscriptionService.service;

import com.example.test_task.subscriptionService.mapper.InvoiceMapper;
import com.example.test_task.subscriptionService.model.dto.InvoiceMessageDto;
import com.example.test_task.subscriptionService.model.dto.InvoiceResponseDto;
import com.example.test_task.subscriptionService.model.entity.Invoices;
import com.example.test_task.subscriptionService.model.entity.Subscriptions;
import com.example.test_task.subscriptionService.model.enums.retryableTask.RetryableTaskType;
import com.example.test_task.subscriptionService.repository.InvoicesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoicesService {
    private final InvoicesRepository invoicesRepository;
    private final RetryableTaskService retryableTaskService;
    private final RabbitMQService rabbitMQService;
    private final InvoiceMapper invoiceMapper;

    @Transactional
    public void createInvoice(Subscriptions subscription, LocalDate invoiceDate) {
        boolean alreadyExists = invoicesRepository.existsBySubscriptionAndInvoiceDate(subscription, invoiceDate);

        if (alreadyExists) {
            log.debug("Счёт за подписку {} уже выставлен {}", subscription.getId(), invoiceDate);
            return;
        }

        Invoices invoice = new Invoices();
        invoice.setUserId(subscription.getUserId());
        invoice.setSubscription(subscription);
        invoice.setInvoiceDate(invoiceDate);
        invoice.setType(subscription.getType());
        invoice.setPrice(subscription.getType().getPrice());
        invoice.setSubscriptionActivationDate(subscription.getActivationDate());

        Invoices saved = invoicesRepository.save(invoice);

        try {
            InvoiceMessageDto messageDto = invoiceMapper.toDto(saved);
            rabbitMQService.sendInvoice(messageDto);
        } catch (Exception e) {
            log.error("Ошибка отправки счёта в брокер, задача отправится повторно", e);
            retryableTaskService.createRetryableTask(saved, RetryableTaskType.SEND_INVOICE);
        }
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getUserInvoices(Long userId, int page, int size) {
        Page<Invoices> pageResult = invoicesRepository.findByUserIdOrderByInvoiceDateDesc(userId, PageRequest.of(page, size));
        return pageResult.getContent().stream()
                .map(InvoiceMapper::toResponseDto)
                .toList();
    }
}
