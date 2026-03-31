package com.example.test_task.cacheservice.service;

import com.example.test_task.cacheservice.dto.UserCacheInfoDto;
import com.example.test_task.cacheservice.repository.UserCacheRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCacheService {
    private final UserCacheRepository userCacheRepository;

    public Optional<UserCacheInfoDto> getUserCacheInfo(Long userId, Pageable pageable) {
        Pageable safePageable = (pageable != null) ? pageable : PageRequest.of(0, 10);
        return userCacheRepository.findByUserId(userId)
                .map(cache -> paginateInvoices(cache, safePageable));
    }

    private static @NonNull UserCacheInfoDto paginateInvoices(UserCacheInfoDto cache, Pageable pageable) {
        List<UserCacheInfoDto.InvoiceInfo> allInvoices = cache.getInvoices();
        if (allInvoices == null || allInvoices.isEmpty()) {
            cache.setInvoices(Collections.emptyList());
            return cache;
        }
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        int start = Math.min((pageNumber * pageSize), allInvoices.size());
        int end = Math.min((start + pageSize), allInvoices.size());

        if (start > end) {
            cache.setInvoices(Collections.emptyList());
        } else {
            cache.setInvoices(allInvoices.subList(start, end));
        }
        return cache;
    }

    public void saveUserCacheInfo(UserCacheInfoDto cacheInfoDto) {
        userCacheRepository.save(cacheInfoDto);
    }
}
