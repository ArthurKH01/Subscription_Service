package com.example.test_task.cacheservice.controller;

import com.example.test_task.cacheservice.dto.UserCacheInfoDto;
import com.example.test_task.cacheservice.service.UserCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserCacheController {
    private final UserCacheService userCacheService;

    @GetMapping("/{userId}/cache")
    public ResponseEntity<UserCacheInfoDto> fetchUserCacheInfo(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return userCacheService.getUserCacheInfo(userId, pageable)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
