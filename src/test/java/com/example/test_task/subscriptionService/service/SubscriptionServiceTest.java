package com.example.test_task.subscriptionService.service;

import com.example.test_task.subscriptionService.model.dto.ActivateRequest;
import com.example.test_task.subscriptionService.model.dto.DeactivateRequest;
import com.example.test_task.subscriptionService.model.enums.subscription.SubscriptionType;
import com.example.test_task.subscriptionService.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {
    private final SubscriptionRepository repo = mock(SubscriptionRepository.class);
    private final SubscriptionsService service = new SubscriptionsService(repo);

    @Test
    void activate_ThrowsIfDateInPast() {
        ActivateRequest req = new ActivateRequest();
        req.setUserId(1L);
        req.setSubscriptionType(SubscriptionType.BASIC);
        req.setActivationDate(LocalDate.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> service.activate(req));
    }

    @Test
    void activate_ThrowsIfUserAlreadyActive() {
        ActivateRequest req = new ActivateRequest();
        req.setUserId(2L);
        req.setSubscriptionType(SubscriptionType.BASIC);
        req.setActivationDate(LocalDate.now().plusDays(1));
        when(repo.existsByUserIdAndStatus(anyLong(), any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.activate(req));
    }

    @Test
    void activate_SavesSubscriptionIfValid() {
        ActivateRequest req = new ActivateRequest();
        req.setUserId(3L);
        req.setSubscriptionType(SubscriptionType.BASIC);
        req.setActivationDate(LocalDate.now().plusDays(1));
        when(repo.existsByUserIdAndStatus(anyLong(), any())).thenReturn(false);

        service.activate(req);

        verify(repo).save(any());
    }

    @Test
    void deactivate_ThrowsIfNotFound() {
        DeactivateRequest req = new DeactivateRequest();
        req.setUserId(1L);
        req.setSubscriptionType(SubscriptionType.PRO);
        when(repo.findByUserIdAndTypeAndStatus(any(), any(), any())).thenReturn(java.util.Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.deactivate(req));
    }
}
