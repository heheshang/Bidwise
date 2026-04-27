package com.ssk.bidwise.dal.redis.oauth2;

import org.springframework.stereotype.Component;

/**
 * OAuth2 刷新令牌 Redis 缓存
 */
@Component
public class OAuth2RefreshTokenRedisCache {

    private static final String KEY_PREFIX = "oauth2:refresh_token:";

    /**
     * 构造 Redis Key
     */
    private String getKey(String refreshToken) {
        return KEY_PREFIX + refreshToken;
    }

    /**
     * 删除刷新令牌
     */
    public void remove(String refreshToken) {
        // TODO: 实现 Redis 删除逻辑
        // redisTemplate.delete(getKey(refreshToken));
    }
}
