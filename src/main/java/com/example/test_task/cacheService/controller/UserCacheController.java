package com.example.test_task.cacheService.controller;

import com.example.test_task.cacheService.dto.UserCacheInfoDto;
import com.example.test_task.cacheService.service.UserCacheService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserCacheController {
    private final UserCacheService userCacheService;

    @GetMapping("/{userId}/cache")
    public ResponseEntity<UserCacheInfoDto> fetchUserCacheInfo(
            @Valid @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userCacheService.getUserCacheInfo(userId, page, size)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
