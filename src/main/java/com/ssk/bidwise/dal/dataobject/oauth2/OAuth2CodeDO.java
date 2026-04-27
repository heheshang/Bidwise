package com.ssk.bidwise.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OAuth2 授权码 DO
 */
@Data
@TableName("oauth2_codes")
public class OAuth2CodeDO {

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
     * 授权码
     */
    private String code;

    /**
     * 重定向 URI
     */
    private String redirectUri;

    /**
     * 授权范围
     */
    private String scope;

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
