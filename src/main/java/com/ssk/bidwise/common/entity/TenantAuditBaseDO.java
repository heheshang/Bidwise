package com.ssk.bidwise.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 带租户的基础实体类
 * 继承 AuditBaseDO，增加 tenantId 字段
 *
 * @author Bidwise
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantAuditBaseDO extends AuditBaseDO {

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 更新者
     */
    private Long updater;

}
