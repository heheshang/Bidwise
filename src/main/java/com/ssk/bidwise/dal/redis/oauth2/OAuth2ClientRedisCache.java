package com.ssk.bidwise.dal.redis.oauth2;

import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2ClientDO;
import org.springframework.stereotype.Component;

/**
 * OAuth2 客户端 Redis 缓存
 * 客户端信息变更不频繁，缓存加速查询
 */
@Component
public class OAuth2ClientRedisCache {

    private static final String KEY_PREFIX = "oauth2:client:";

    /**
     * 构造 Redis Key
     */
    private String getKey(String clientId) {
        return KEY_PREFIX + clientId;
    }

    /**
     * 获取客户端
     */
    public OAuth2ClientDO get(String clientId) {
        // TODO: 实现 Redis 获取逻辑
        // return redisTemplate.opsForValue().get(getKey(clientId));
        return null;
    }

    /**
     * 保存客户端
     */
    public void save(OAuth2ClientDO client, long expireSeconds) {
        // TODO: 实现 Redis 保存逻辑
        // redisTemplate.opsForValue().set(getKey(client.getClientId()), client, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 删除客户端缓存
     */
    public void remove(String clientId) {
        // TODO: 实现 Redis 删除逻辑
        // redisTemplate.delete(getKey(clientId));
    }
}
