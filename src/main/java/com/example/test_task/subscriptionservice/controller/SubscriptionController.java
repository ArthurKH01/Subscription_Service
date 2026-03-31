package com.example.test_task.subscriptionservice.controller;

import com.example.test_task.subscriptionservice.model.dto.ActivateRequest;
import com.example.test_task.subscriptionservice.model.dto.DeactivateRequest;
import com.example.test_task.subscriptionservice.model.dto.ResponseDto;
import com.example.test_task.subscriptionservice.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.test_task.subscriptionservice.constants.SubscriptionConstants.*;

@RestController
@RequestMapping(value = "/api/subscriptions", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/activate")
    public ResponseEntity<ResponseDto> activateSubscription(@Valid @RequestBody ActivateRequest request) {
        subscriptionService.activate(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(STATUS_201, MESSAGE_201));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<ResponseDto> deactivateSubscription(@Valid @RequestBody DeactivateRequest request) {
        subscriptionService.deactivate(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(STATUS_200, MESSAGE_200));
    }
}
