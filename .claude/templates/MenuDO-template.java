// .claude/templates/MenuDO-template.java
package com.example.auth.dal.dataobject.permission;

import com.example.auth.framework.mybatis.core.dataobject.AuditBaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单 DO
 *
 * 菜单全局共享，不受租户过滤
 */
@TableName("sys_menu")
@IdSequence("sys_menu_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@SkipTenantFilter  // 菜单全局共享
public class MenuDO extends AuditBaseDO {

    @TableId
    private Long id;

    /** 菜单名称 */
    private String name;

    /** 权限标识（如 system:user:create） */
    private String permission;

    /** 菜单类型（1=目录 2=菜单 3=按钮） */
    private Integer type;

    /** 显示顺序 */
    private Integer sort;

    /** 父菜单ID（0=根节点） */
    private Long parentId;

    /** 路由地址 */
    private String path;

    /** 菜单图标 */
    private String icon;

    /** 组件路径 */
    private String component;

    /** 组件名 */
    private String componentName;

    /** 状态（0开启 1关闭） */
    private Integer status;

    /** 是否可见 */
    private Boolean visible;

    /** 是否缓存 */
    private Boolean keepAlive;

    /** 是否总是显示 */
    private Boolean alwaysShow;
}
