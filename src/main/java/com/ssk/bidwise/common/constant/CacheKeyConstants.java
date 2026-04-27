package com.ssk.bidwise.common.constant;

/**
 * 缓存 Key 常量
 *
 * @author Bidwise
 */
public interface CacheKeyConstants {

    /**
     * 权限缓存前缀 - 用户权限
     */
    String PERMISSION_USER_PREFIX = "permission:user:";

    /**
     * OAuth2 客户端缓存
     */
    String OAUTH2_CLIENT = "oauth2:client:";

    /**
     * OAuth2 访问令牌缓存
     */
    String OAUTH2_ACCESS_TOKEN = "oauth2:access-token:";

    /**
     * OAuth2 刷新令牌缓存
     */
    String OAUTH2_REFRESH_TOKEN = "oauth2:refresh-token:";

}
