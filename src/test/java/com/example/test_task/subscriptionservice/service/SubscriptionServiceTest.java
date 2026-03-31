package com.example.test_task.subscriptionservice.service;

import com.example.test_task.subscriptionservice.exception.ActiveSubscriptionNotFoundException;
import com.example.test_task.subscriptionservice.exception.SubscriptionAlreadyActiveException;
import com.example.test_task.subscriptionservice.model.dto.ActivateRequest;
import com.example.test_task.subscriptionservice.model.dto.DeactivateRequest;
import com.example.test_task.subscriptionservice.model.entity.Subscription;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionStatus;
import com.example.test_task.subscriptionservice.model.enums.subscription.SubscriptionType;
import com.example.test_task.subscriptionservice.repository.SubscriptionRepository;
import com.example.test_task.subscriptionservice.service.activation.SubscriptionFactory;
import com.example.test_task.subscriptionservice.service.activation.SubscriptionUpdater;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    private static final Long ID = 2L;
    private static final Long USER_ID = 1L;
    private static final SubscriptionType TYPE = SubscriptionType.BASIC;
    private static final LocalDate ACTIVATION_DATE = LocalDate.now();
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private SubscriptionUpdater subscriptionUpdater;
    @Mock
    private SubscriptionFactory subscriptionFactory;
    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void activate_shouldThrowException_whenActiveSubscriptionExists() {
        ActivateRequest request = new ActivateRequest(USER_ID, TYPE, ACTIVATION_DATE);
        when(subscriptionRepository.existsByUserIdAndStatus(USER_ID, SubscriptionStatus.ACTIVE))
                .thenReturn(true);

        assertThrows(SubscriptionAlreadyActiveException.class, () -> subscriptionService.activate(request));

        verify(subscriptionRepository, never()).findByUserIdAndTypeAndStatus(any(), any(), any());
        verify(subscriptionUpdater, never()).update(any(), any());
        verify(subscriptionFactory, never()).createNewSubscription(any(), any(), any());
    }

    @Test
    void activate_shouldUpdateExistingInactiveSubscription_whenExists() {
        ActivateRequest request = new ActivateRequest(USER_ID, TYPE, ACTIVATION_DATE);
        Subscription subscription = new Subscription();
        subscription.setId(ID);
        subscription.setUserId(USER_ID);
        subscription.setType(TYPE);
        subscription.setStatus(SubscriptionStatus.INACTIVE);

        when(subscriptionRepository.existsByUserIdAndStatus(USER_ID, SubscriptionStatus.ACTIVE))
                .thenReturn(false);
        when(subscriptionRepository.findByUserIdAndTypeAndStatus(USER_ID, TYPE, SubscriptionStatus.INACTIVE))
                .thenReturn(Optional.of(subscription));

        subscriptionService.activate(request);
        verify(subscriptionUpdater).update(subscription, ACTIVATION_DATE);
        verify(subscriptionFactory, never()).createNewSubscription(any(), any(), any());
    }

    @Test
    void activate_shouldCreateNewSubscription_WhenNoInactiveSubscription() {
        ActivateRequest request = new ActivateRequest(USER_ID, TYPE, ACTIVATION_DATE);
        Subscription subscription = new Subscription();
        subscription.setId(ID);
        when(subscriptionRepository.existsByUserIdAndStatus(USER_ID, SubscriptionStatus.ACTIVE))
                .thenReturn(false);
        when(subscriptionRepository.findByUserIdAndTypeAndStatus(USER_ID, TYPE, SubscriptionStatus.INACTIVE))
                .thenReturn(Optional.empty());
        when(subscriptionFactory.createNewSubscription(USER_ID, TYPE, ACTIVATION_DATE))
                .thenReturn(subscription);

        subscriptionService.activate(request);

        verify(subscriptionUpdater, never()).update(any(), any());
        verify(subscriptionFactory).createNewSubscription(USER_ID, TYPE, ACTIVATION_DATE);
    }

    @Test
    void deactivate_shouldThrowException_whenActiveSubscriptionExists() {
        DeactivateRequest request = new DeactivateRequest(USER_ID, TYPE);
        Subscription subscription = new Subscription();
        subscription.setId(ID);
        subscription.setUserId(USER_ID);
        subscription.setType(TYPE);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        when(subscriptionRepository.findByUserIdAndTypeAndStatus(USER_ID, TYPE, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.of(subscription));
        subscriptionService.deactivate(request);
        assertEquals(SubscriptionStatus.INACTIVE, subscription.getStatus());
    }

    @Test
    void deactivate_shouldThrowException_whenNoActiveSubscriptionExists() {
        DeactivateRequest request = new DeactivateRequest(USER_ID, TYPE);
        when(subscriptionRepository.findByUserIdAndTypeAndStatus(USER_ID, TYPE, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.empty());
        assertThrows(ActiveSubscriptionNotFoundException.class, () -> subscriptionService.deactivate(request));
    }
}
