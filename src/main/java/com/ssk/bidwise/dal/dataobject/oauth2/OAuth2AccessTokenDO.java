package com.ssk.bidwise.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OAuth2 访问令牌 DO
 */
@Data
@TableName("oauth2_access_tokens")
public class OAuth2AccessTokenDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 客户端 ID
     */
    private Long clientId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 过期时间
     */
    private LocalDateTime expirationTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;
}
