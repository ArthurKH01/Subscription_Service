package com.example.test_task.subscriptionservice.model.dto;

import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public record ActivateRequest(
        @NotNull
        Long userId,

        @NotNull
        SubscriptionType type,

        @NotNull
        @FutureOrPresent(message = "Дата активации не может быть в прошлом")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate activationDate
) {
}
