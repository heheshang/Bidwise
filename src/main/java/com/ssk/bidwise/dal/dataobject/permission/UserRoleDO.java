package com.ssk.bidwise.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.*;
import com.ssk.bidwise.common.entity.AuditBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色关联 DO
 *
 * @author Bidwise
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user_role", autoResultMap = true)
public class UserRoleDO extends AuditBaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

}
