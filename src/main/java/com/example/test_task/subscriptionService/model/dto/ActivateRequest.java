package com.example.test_task.subscriptionService.model.dto;

import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActivateRequest {
    @NotNull
    private Long userId;
    @NotNull
    private SubscriptionType subscriptionType;
    @NotNull
    @FutureOrPresent(message = "Дата активации не может быть в прошлом")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate activationDate;
}
