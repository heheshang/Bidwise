package com.ssk.bidwise.dal.redis.oauth2;

import org.springframework.stereotype.Component;

/**
 * OAuth2 授权码 Redis 缓存
 */
@Component
public class OAuth2CodeRedisCache {

    private static final String KEY_PREFIX = "oauth2:code:";

    /**
     * 构造 Redis Key
     */
    private String getKey(String code) {
        return KEY_PREFIX + code;
    }

    /**
     * 删除授权码（使用后）
     */
    public void remove(String code) {
        // TODO: 实现 Redis 删除逻辑
        // redisTemplate.delete(getKey(code));
    }
}
