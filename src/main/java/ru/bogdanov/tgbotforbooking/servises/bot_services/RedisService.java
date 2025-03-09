package ru.bogdanov.tgbotforbooking.servises.bot_services;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final String USERS = "users";

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheUser(String username) {
        redisTemplate.opsForSet().add(USERS, username);
    }

    public boolean isUserCached(String username) {
        return redisTemplate.opsForSet().isMember(USERS, username);
    }

    public void removeUser(String username) {
        redisTemplate.opsForSet().remove(USERS, username);
    }

}
