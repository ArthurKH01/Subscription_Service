package com.example.test_task.subscriptionService.controller;

import com.example.test_task.subscriptionService.constants.SubscriptionConstants;
import com.example.test_task.subscriptionService.model.dto.ActivateRequest;
import com.example.test_task.subscriptionService.model.dto.DeactivateRequest;
import com.example.test_task.subscriptionService.model.dto.ResponseDto;
import com.example.test_task.subscriptionService.service.SubscriptionsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/subscriptions", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
public class SubscriptionController {
    private final SubscriptionsService subscriptionsService;

    @PostMapping("/activate")
    public ResponseEntity<ResponseDto> activateSubscription(@Valid @RequestBody ActivateRequest request) {
        subscriptionsService.activate(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(SubscriptionConstants.STATUS_201, SubscriptionConstants.MESSAGE_201));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<ResponseDto> deactivateSubscription(@Valid @RequestBody DeactivateRequest request) {
        subscriptionsService.deactivate(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(SubscriptionConstants.STATUS_200, SubscriptionConstants.MESSAGE_200));
    }
}
