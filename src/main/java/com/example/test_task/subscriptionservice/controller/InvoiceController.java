package com.example.test_task.subscriptionservice.controller;

import com.example.test_task.subscriptionservice.model.dto.InvoiceResponseDto;
import com.example.test_task.subscriptionservice.service.InvoicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/invoices", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoicesService invoicesService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<InvoiceResponseDto>> fetchUserInvoices(@PathVariable Long userId,
                                                                      @PageableDefault(page = 0, size = 10)Pageable pageable) {
        List<InvoiceResponseDto> responseDtoList = invoicesService.getUserInvoices(userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
