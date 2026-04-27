// .claude/templates/RoleDO-template.java
package com.example.auth.dal.dataobject.permission;

import com.example.auth.framework.mybatis.core.dataobject.TenantAuditBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

/**
 * 角色 DO
 */
@TableName(value = "sys_role", autoResultMap = true)
@IdSequence("sys_role_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDO extends TenantAuditBaseDO {

    @TableId
    private Long id;

    /** 角色名称 */
    private String name;

    /** 角色标识（如 super_admin, tenant_admin） */
    private String code;

    /** 显示顺序 */
    private Integer sort;

    /** 状态（0开启 1关闭） */
    private Integer status;

    /**
     * 角色类型
     * 枚举 {@link RoleTypeEnum}
     * 1 = 内置角色（不可删除）
     * 2 = 自定义角色
     */
    private Integer type;

    /** 备注 */
    private String remark;

    /**
     * 数据范围
     * 枚举 {@link DataScopeEnum}
     */
    private Integer dataScope;

    /**
     * 数据范围指定部门数组（JSON）
     * 仅 dataScope = DEPT_CUSTOM 时使用
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Long> dataScopeDeptIds;
}
