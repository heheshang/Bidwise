package com.ssk.bidwise.dal.redis.oauth2;

import com.ssk.bidwise.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import org.springframework.stereotype.Component;

/**
 * OAuth2 访问令牌 Redis 缓存
 * 用于加速访问令牌的验证
 */
@Component
public class OAuth2AccessTokenRedisCache {

    private static final String KEY_PREFIX = "oauth2:access_token:";

    /**
     * 构造 Redis Key
     */
    private String getKey(String accessToken) {
        return KEY_PREFIX + accessToken;
    }

    /**
     * 保存访问令牌
     */
    public void save(OAuth2AccessTokenDO accessTokenDO, long expireSeconds) {
        // TODO: 实现 Redis 保存逻辑
        // redisTemplate.opsForValue().set(getKey(accessTokenDO.getAccessToken()), accessTokenDO, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 获取访问令牌
     */
    public OAuth2AccessTokenDO get(String accessToken) {
        // TODO: 实现 Redis 获取逻辑
        // return redisTemplate.opsForValue().get(getKey(accessToken));
        return null;
    }

    /**
     * 删除访问令牌
     */
    public void remove(String accessToken) {
        // TODO: 实现 Redis 删除逻辑
        // redisTemplate.delete(getKey(accessToken));
    }
}
