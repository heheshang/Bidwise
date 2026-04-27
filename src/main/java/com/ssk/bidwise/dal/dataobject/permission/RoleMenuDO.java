package com.ssk.bidwise.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.*;
import com.ssk.bidwise.common.entity.TenantAuditBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色菜单关联 DO
 *
 * @author Bidwise
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_role_menu", autoResultMap = true)
public class RoleMenuDO extends TenantAuditBaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
