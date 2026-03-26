package com.example.test_task.subscriptionService.model.dto;

import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeactivateRequest {
    @NotNull
    private Long userId;
    @NotNull
    private SubscriptionType subscriptionType;
}
