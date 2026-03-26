package com.example.test_task.cacheService.repository;

import com.example.test_task.cacheService.dto.UserCacheInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {
    private final RedisTemplate<String, UserCacheInfoDto> redisTemplate;

    public Optional<UserCacheInfoDto> findByUserId(Long userId) {
        try {
            UserCacheInfoDto cacheDto = redisTemplate.opsForValue().get(userId.toString());
            return Optional.ofNullable(cacheDto);
        } catch (DataAccessException e) {
            log.error("Ошибка чтения из Redis пользователя {}: {}", userId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    public void save(UserCacheInfoDto cacheInfoDto) {
        try {
            redisTemplate.opsForValue().set(cacheInfoDto.getUserId().toString(), cacheInfoDto);
        } catch (DataAccessException  e) {
            log.error("Ошибка записи в Redis для пользователя {}: {}", cacheInfoDto.getUserId(), e.getMessage(), e);
        }
    }

}
