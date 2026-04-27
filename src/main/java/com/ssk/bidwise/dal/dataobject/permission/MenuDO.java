package com.ssk.bidwise.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.*;
import com.ssk.bidwise.common.entity.AuditBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单 DO
 *
 * @author Bidwise
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_menu", autoResultMap = true)
public class MenuDO extends AuditBaseDO {

    /**
     * 菜单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 菜单类型 1目录 2菜单 3按钮
     */
    private Integer type;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 组件名
     */
    private String componentName;

    /**
     * 状态 0开启 1关闭
     */
    private Integer status;

    /**
     * 是否可见
     */
    private Boolean visible;

    /**
     * 是否缓存
     */
    private Boolean keepAlive;

    /**
     * 是否总是显示
     */
    private Boolean alwaysShow;

}
