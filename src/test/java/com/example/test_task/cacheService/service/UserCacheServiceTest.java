package com.example.test_task.cacheService.service;

import com.example.test_task.cacheService.dto.UserCacheInfoDto;
import com.example.test_task.cacheService.repository.UserCacheRepository;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserCacheServiceTest {
    UserCacheRepository repo = mock(UserCacheRepository.class);
    UserCacheService service = new UserCacheService(repo);

    @Test
    void getUserCacheInfo_ReturnsCacheIfExists() {
        UserCacheInfoDto dto = new UserCacheInfoDto();
        dto.setInvoices(Collections.emptyList());
        when(repo.findByUserId(1L)).thenReturn(Optional.of(dto));

        Optional<UserCacheInfoDto> res = service.getUserCacheInfo(1L, 0, 10);
        assertTrue(res.isPresent());
    }

    @Test
    void saveUserCacheInfo_DelegatesToRepo() {
        UserCacheInfoDto dto = new UserCacheInfoDto();
        service.saveUserCacheInfo(dto);
        verify(repo).save(dto);
    }
}
