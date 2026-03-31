package com.example.test_task.subscriptionservice.model.entity;

import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long userId;

    private LocalDate invoiceDate;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    private LocalDate subscriptionActivationDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
}
