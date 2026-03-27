package com.example.test_task.subscriptionService.controller;

import com.example.test_task.subscriptionService.model.dto.InvoiceResponseDto;
import com.example.test_task.subscriptionService.service.InvoicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/invoices", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class InvoiceController {
    private final InvoicesService invoicesService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<InvoiceResponseDto>> fetchUserInvoices(@Valid @PathVariable Long userId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        List<InvoiceResponseDto> responseDtoList = invoicesService.getUserInvoices(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
