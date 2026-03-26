package com.example.test_task.subscriptionService.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceMessageDto {
    private Long id;
    private Long userId;
    private LocalDate invoiceDate;
    private BigDecimal price;
    private String subscriptionType;
    private LocalDate subscriptionActivationDate;
}
