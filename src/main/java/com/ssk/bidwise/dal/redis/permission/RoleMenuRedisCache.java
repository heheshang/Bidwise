package com.ssk.bidwise.dal.redis.permission;

import com.ssk.bidwise.common.constant.CacheKeyConstants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * 角色菜单权限 Redis Cache
 * 用于缓存用户的权限标识列表，提升权限校验速度
 *
 * @author Bidwise
 */
@Repository
public class RoleMenuRedisCache {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 清除所有权限缓存
     * 权限变更时调用
     */
    public void clearAll() {
        // 使用 pattern 删除所有相关缓存
        String pattern = CacheKeyConstants.PERMISSION_USER_PREFIX + "*";
        stringRedisTemplate.keys(pattern).forEach(key -> stringRedisTemplate.delete(key));
    }

    /**
     * 清除指定用户的权限缓存
     *
     * @param userId 用户编号
     */
    public void clearUserPermission(Long userId) {
        String key = CacheKeyConstants.PERMISSION_USER_PREFIX + userId;
        stringRedisTemplate.delete(key);
    }

}
