package com.example.test_task.subscriptionservice.model.dto;

import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionType;
import jakarta.validation.constraints.NotNull;


public record DeactivateRequest(
        @NotNull
        Long userId,
        @NotNull
        SubscriptionType type
) {
}

