package com.example.test_task.cacheService.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserCacheInfoDto {
    private Long userId;
    private List<SubscriptionInfo> subscriptionList;
    private List<InvoiceInfo> invoices;

    @Data
    public static class SubscriptionInfo{
        private String type;
        private String status;
        private String activationDate;
    }

    @Data
    public static class InvoiceInfo{
        private Long id;
        private String invoiceDate;
        private String subscriptionType;
        private String subscriptionActivationDate;
        private String price;
    }
}
