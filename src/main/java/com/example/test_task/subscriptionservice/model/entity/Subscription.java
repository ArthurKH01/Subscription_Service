package com.example.test_task.subscriptionservice.model.entity;

import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SubscriptionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    private LocalDate activationDate;

    private LocalDate nextInvoiceDate;

    @OneToMany(mappedBy = "subscription")
    private List<Invoice> invoices;
}
