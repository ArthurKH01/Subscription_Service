package com.example.test_task.subscriptionservice.service;

import com.example.test_task.subscriptionservice.mapper.InvoiceMapper;
import com.example.test_task.subscriptionservice.model.dto.InvoiceMessageDto;
import com.example.test_task.subscriptionservice.model.dto.InvoiceResponseDto;
import com.example.test_task.subscriptionservice.model.entity.Invoice;
import com.example.test_task.subscriptionservice.model.entity.Subscription;
import com.example.test_task.subscriptionservice.model.enums.retryableTask.RetryableTaskType;
import com.example.test_task.subscriptionservice.repository.InvoiceRepository;
import com.example.test_task.subscriptionservice.service.invoice.InvoiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoicesService {
    private final InvoiceRepository invoiceRepository;
    private final RetryableTaskService retryableTaskService;
    private final RabbitMQService rabbitMQService;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceFactory invoiceFactory;

    @Transactional
    public void createInvoice(Subscription subscription, LocalDate invoiceDate) {
        boolean alreadyExists = invoiceRepository.existsBySubscriptionAndInvoiceDate(subscription, invoiceDate);

        if (alreadyExists) {
            log.debug("Счёт за подписку {} уже выставлен {}", subscription.getId(), invoiceDate);
            return;
        }

        Invoice invoice = invoiceFactory.createNewInvoice(subscription, invoiceDate);
        Invoice saved = invoiceRepository.save(invoice);

        try {
            InvoiceMessageDto messageDto = invoiceMapper.toDto(saved);
            rabbitMQService.sendInvoice(messageDto);
        } catch (Exception e) {
            log.error("Ошибка отправки счёта в брокер, задача отправится повторно", e);
            retryableTaskService.createRetryableTask(saved, RetryableTaskType.SEND_INVOICE);
        }
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getUserInvoices(Long userId, Pageable pageable) {
        Page<Invoice> pageResult = invoiceRepository.findByUserId(userId, pageable);
        return pageResult.getContent().stream()
                .map(invoiceMapper::toResponseDto)
                .toList();
    }
}
