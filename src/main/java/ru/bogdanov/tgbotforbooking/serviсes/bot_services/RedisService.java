package ru.bogdanov.tgbotforbooking.serviсes.bot_services;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final String USERS = "user_ids";

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheUser(Long userId) {
        redisTemplate.opsForSet().add(USERS, userId.toString());
    }

    public boolean isUserCached(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(USERS, userId.toString()));
    }

}

