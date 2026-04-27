package com.ssk.bidwise.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OAuth2 刷新令牌 DO
 */
@Data
@TableName("oauth2_refresh_tokens")
public class OAuth2RefreshTokenDO {

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
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 关联的访问令牌 ID
     */
    private Long accessTokenId;

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
