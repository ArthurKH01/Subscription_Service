package com.example.test_task.subscriptionService.model.entity;

import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Invoices {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long userId;

    private LocalDate invoiceDate;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    private LocalDate subscriptionActivationDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscriptions subscription;
}
