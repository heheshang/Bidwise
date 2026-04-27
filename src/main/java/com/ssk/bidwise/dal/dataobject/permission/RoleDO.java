package com.ssk.bidwise.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.*;
import com.ssk.bidwise.common.entity.TenantAuditBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色 DO
 *
 * @author Bidwise
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_role", autoResultMap = true)
public class RoleDO extends TenantAuditBaseDO {

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色标识
     */
    private String code;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 状态 0开启 1关闭
     */
    private Integer status;

    /**
     * 角色类型 1系统内置 2自定义
     */
    private Integer type;

    /**
     * 数据范围
     * 1全部 2自定义部门 3本部门 4本部门及子部门 5仅本人
     */
    private Integer dataScope;

    /**
     * 数据范围指定部门（JSON数组）
     */
    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private java.util.List<Long> dataScopeDeptIds;

    /**
     * 备注
     */
    private String remark;

}
