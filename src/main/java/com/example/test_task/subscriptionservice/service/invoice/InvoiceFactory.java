package com.example.test_task.subscriptionservice.service.invoice;

import com.example.test_task.subscriptionservice.model.entity.Invoice;
import com.example.test_task.subscriptionservice.model.entity.Subscription;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InvoiceFactory {
    public Invoice createNewInvoice(Subscription subscription, LocalDate invoiceDate) {
        Invoice invoice = new Invoice();
        invoice.setUserId(subscription.getUserId());
        invoice.setSubscription(subscription);
        invoice.setInvoiceDate(invoiceDate);
        invoice.setSubscriptionType(subscription.getType());
        invoice.setPrice(subscription.getType().getPrice());
        invoice.setSubscriptionActivationDate(subscription.getActivationDate());

        return invoice;
    }
}
