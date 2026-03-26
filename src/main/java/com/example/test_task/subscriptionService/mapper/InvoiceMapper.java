package com.example.test_task.subscriptionService.mapper;

import com.example.test_task.subscriptionService.model.dto.InvoiceMessageDto;
import com.example.test_task.subscriptionService.model.dto.InvoiceResponseDto;
import com.example.test_task.subscriptionService.model.entity.InvoiceInfo;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {
    public InvoiceMessageDto toDto(InvoiceInfo invoice) {
        InvoiceMessageDto messageDto = new InvoiceMessageDto();
        messageDto.setId(invoice.getId());
        messageDto.setUserId(invoice.getUserId());
        messageDto.setInvoiceDate(invoice.getInvoiceDate());
        messageDto.setPrice(invoice.getPrice());
        messageDto.setSubscriptionType(invoice.getType().toString());
        messageDto.setSubscriptionActivationDate(invoice.getSubscriptionActivationDate());
        return messageDto;
    }

    public static InvoiceResponseDto toResponseDto(InvoiceInfo invoice){
        InvoiceResponseDto responseDto = new InvoiceResponseDto();
        responseDto.setId(invoice.getId());
        responseDto.setUserId(invoice.getUserId());
        responseDto.setInvoiceDate(invoice.getInvoiceDate());
        responseDto.setPrice(invoice.getPrice());
        responseDto.setSubscriptionType(invoice.getType().toString());
        responseDto.setSubscriptionActivationDate(invoice.getSubscriptionActivationDate());
        return responseDto;
    }
}
