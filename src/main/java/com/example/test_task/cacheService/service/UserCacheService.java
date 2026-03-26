package com.example.test_task.cacheService.service;

import com.example.test_task.cacheService.dto.UserCacheInfoDto;
import com.example.test_task.cacheService.repository.UserCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCacheService {
    private final UserCacheRepository userCacheRepository;

    public Optional<UserCacheInfoDto> getUserCacheInfo(Long userId, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        int finalSize = size;
        int finalPage = page;
        return userCacheRepository.findByUserId(userId)
                .map(cache -> {
                    List<UserCacheInfoDto.InvoiceInfo> allInvoices = cache.getInvoices();
                    if (allInvoices == null || allInvoices.isEmpty()) {
                        cache.setInvoices(Collections.emptyList());
                        return cache;
                    }
                    int start = Math.min((finalPage * finalSize), allInvoices.size());
                    int end = Math.min((start+ finalSize), allInvoices.size());
                    if (start > end) {
                        cache.setInvoices(Collections.emptyList());
                    } else {
                        cache.setInvoices(allInvoices.subList(start, end));
                    }
                    return cache;
                });
    }

    public void saveUserCacheInfo(UserCacheInfoDto cacheInfoDto){
        userCacheRepository.save(cacheInfoDto);
    }
}
