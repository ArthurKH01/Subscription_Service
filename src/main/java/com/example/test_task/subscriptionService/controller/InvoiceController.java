package com.example.test_task.subscriptionService.controller;

import com.example.test_task.subscriptionService.model.dto.InvoiceResponseDto;
import com.example.test_task.subscriptionService.service.InvoicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    public List<InvoiceResponseDto> getUserInvoice(@Valid @PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return invoicesService.getUserInvoices(userId, page, size);
    }
}
