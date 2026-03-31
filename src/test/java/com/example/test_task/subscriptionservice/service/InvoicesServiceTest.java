package com.example.test_task.subscriptionservice.service;

import com.example.test_task.subscriptionservice.exception.InvoiceSendingException;
import com.example.test_task.subscriptionservice.mapper.InvoiceMapper;
import com.example.test_task.subscriptionservice.model.dto.InvoiceMessageDto;
import com.example.test_task.subscriptionservice.model.dto.InvoiceResponseDto;
import com.example.test_task.subscriptionservice.model.entity.Invoice;
import com.example.test_task.subscriptionservice.model.entity.Subscription;
import com.example.test_task.subscriptionservice.model.enums.retryableTask.RetryableTaskType;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionType;
import com.example.test_task.subscriptionservice.repository.InvoiceRepository;
import com.example.test_task.subscriptionservice.service.invoice.InvoiceFactory;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoicesServiceTest {
    private static final Long ID = 1L;
    private static final Long SUBSCRIPTION_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final SubscriptionType TYPE = SubscriptionType.BASIC;
    private static final LocalDate INVOICE_DATE = LocalDate.now();
    private static final BigDecimal PRICE = BigDecimal.valueOf(100);

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private RetryableTaskService retryableTaskService;
    @Mock
    private RabbitMQService rabbitMQService;
    @Mock
    private InvoiceMapper invoiceMapper;
    @Mock
    private InvoiceFactory invoiceFactory;
    @InjectMocks
    private InvoicesService invoicesService;

    private Subscription subscription;
    private Invoice invoice;
    private Invoice savedInvoice;
    private InvoiceMessageDto messageDto;

    @Test
    void createInvoice_shouldNotCreateDuplicateInvoice() {
        Subscription subscription = createSubscription();

        when(invoiceRepository.existsBySubscriptionAndInvoiceDate(subscription, INVOICE_DATE))
                .thenReturn(true);
        invoicesService.createInvoice(subscription, INVOICE_DATE);

        verify(invoiceRepository).existsBySubscriptionAndInvoiceDate(subscription, INVOICE_DATE);
        verify(invoiceRepository, never()).save(any());
        verify(rabbitMQService, never()).sendInvoice(any());
        verify(retryableTaskService, never()).createRetryableTask(any(), any());
    }

    @Test
    void createInvoice_shouldCreateInvoiceAndSendToRabbitMQ() {
        Subscription subscription = createSubscription();
        InvoiceMessageDto invoiceMessageDto = new InvoiceMessageDto();

        Invoice invoice = createInvoice();

        when(invoiceRepository.existsBySubscriptionAndInvoiceDate(subscription, INVOICE_DATE))
                .thenReturn(false);
        when(invoiceFactory.createNewInvoice(subscription, INVOICE_DATE))
                .thenReturn(invoice);
        when(invoiceRepository.save(invoice))
                .thenReturn(invoice);
        when(invoiceMapper.toDto(invoice))
                .thenReturn(invoiceMessageDto);

        invoicesService.createInvoice(subscription, INVOICE_DATE);
        verify(invoiceRepository).existsBySubscriptionAndInvoiceDate(subscription, INVOICE_DATE);
        verify(invoiceRepository).save(invoice);
        verify(rabbitMQService).sendInvoice(invoiceMessageDto);
        verify(retryableTaskService).createRetryableTask(any(), any());
    }

    @Test
    void createInvoice_shouldCreateRetryableTaskWhenRabbitMQFails() {
        Subscription subscription = createSubscription();
        Invoice invoice = createInvoice();

        when(invoiceRepository.existsBySubscriptionAndInvoiceDate(subscription, INVOICE_DATE))
                .thenReturn(false);
        when(invoiceFactory.createNewInvoice(subscription, INVOICE_DATE))
                .thenReturn(invoice);
        when(invoiceRepository.save(invoice))
                .thenReturn(invoice);
        doThrow(new InvoiceSendingException("Ошибка при отправке счёта в RabbitMQ", null))
                .when(rabbitMQService).sendInvoice(any());

        invoicesService.createInvoice(subscription, INVOICE_DATE);
        verify(invoiceRepository).existsBySubscriptionAndInvoiceDate(subscription, INVOICE_DATE);
        verify(invoiceRepository).save(invoice);
        verify(rabbitMQService).sendInvoice(any());

        ArgumentCaptor<Invoice> invoiceArgumentCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(retryableTaskService).createRetryableTask(invoiceArgumentCaptor.capture(), eq(RetryableTaskType.SEND_INVOICE));
        assertEquals(invoiceArgumentCaptor.getValue(), invoice);
    }

    @Test
    void getUserInvoices_shouldReturnListOfInvoiceResponseDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Invoice invoice = createInvoice();
        InvoiceResponseDto responseDto = new InvoiceResponseDto();
        Page<Invoice> invoicePage = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findByUserId(USER_ID, pageable))
                .thenReturn(invoicePage);
        when(invoiceMapper.toResponseDto(invoice))
                .thenReturn(responseDto);

        List<InvoiceResponseDto> userInvoices = invoicesService.getUserInvoices(USER_ID, pageable);

        assertEquals(1, userInvoices.size());
        assertEquals(userInvoices.getFirst(), responseDto);

        verify(invoiceRepository).findByUserId(USER_ID, pageable);
        verify(invoiceMapper).toResponseDto(invoice);
    }

    @Test
    void getUserInvoices_shouldReturnEmptyListWhenNoInvoices() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Invoice> invoicePage = new PageImpl<>(Collections.emptyList());

        when(invoiceRepository.findByUserId(USER_ID, pageable))
                .thenReturn(invoicePage);

        List<InvoiceResponseDto> userInvoices = invoicesService.getUserInvoices(USER_ID, pageable);

        assertEquals(0, userInvoices.size());
        verify(invoiceRepository).findByUserId(USER_ID, pageable);
        verify(invoiceMapper, never()).toResponseDto(any());
    }

    private static @NonNull Subscription createSubscription() {
        Subscription subscription = new Subscription();
        subscription.setUserId(SUBSCRIPTION_ID);
        subscription.setType(TYPE);
        subscription.setActivationDate(INVOICE_DATE.minusMonths(1));
        return subscription;
    }

    private static @NonNull Invoice createInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId(ID);
        invoice.setUserId(USER_ID);
        invoice.setInvoiceDate(INVOICE_DATE);
        invoice.setPrice(PRICE);
        invoice.setSubscriptionType(TYPE);
        return invoice;
    }
}


