package com.ssk.bidwise.dal.dataobject.oauth2;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * OAuth2 授权批准记录 DO
 */
@Data
@TableName("oauth2_approvals")
public class OAuth2ApprovalDO {

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
     * 授权范围
     */
    private String scope;

    /**
     * 状态
     * 0 - 拒绝，1 - 批准
     */
    private Integer status;

    /**
     * 过期时间
     */
    private LocalDateTime expiresAt;

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
