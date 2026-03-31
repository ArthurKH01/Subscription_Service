package com.example.test_task.subscriptionservice.model.enums.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum SubscriptionType {
    BASIC(new BigDecimal("100")),
    PRO(new BigDecimal("200"));

    private final BigDecimal price;
}
