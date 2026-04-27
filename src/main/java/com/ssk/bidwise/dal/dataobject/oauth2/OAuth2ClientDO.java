package com.ssk.bidwise.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OAuth2 客户端 DO
 */
@Data
@TableName("oauth2_clients")
public class OAuth2ClientDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 客户端 ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 访问令牌有效期（秒）
     */
    private Integer accessTokenValiditySeconds;

    /**
     * 刷新令牌有效期（秒）
     */
    private Integer refreshTokenValiditySeconds;

    /**
     * 客户端描述
     */
    private String description;

    /**
     * 重定向 URI 列表，多个用逗号分隔
     */
    private String redirectUris;

    /**
     * 授权类型列表，多个用逗号分隔
     */
    private String authorizedGrantTypes;

    /**
     * 授权范围
     */
    private String scope;

    /**
     * 是否自动批准
     * 0 - 需要用户批准，1 - 自动批准
     */
    private Integer autoApprove;

    /**
     * 状态
     * 0 - 禁用，1 - 启用
     */
    private Integer status;

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
