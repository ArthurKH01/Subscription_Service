package com.example.test_task.subscriptionservice.mapper;

import com.example.test_task.subscriptionservice.model.dto.InvoiceMessageDto;
import com.example.test_task.subscriptionservice.model.dto.InvoiceResponseDto;
import com.example.test_task.subscriptionservice.model.entity.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceMessageDto toDto(Invoice invoice);

    InvoiceResponseDto toResponseDto(Invoice invoice);
}
