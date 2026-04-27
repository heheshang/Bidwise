package com.ssk.bidwise.controller.admin.permission.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单 Response VO
 *
 * @author Bidwise
 */
@Data
@Schema(description = "菜单信息")
public class MenuRespVO {

    @Schema(description = "菜单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "用户管理")
    private String name;

    @Schema(description = "权限标识", example = "system:user:create")
    private String permission;

    @Schema(description = "菜单类型 1目录 2菜单 3按钮", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sort;

    @Schema(description = "父菜单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Long parentId;

    @Schema(description = "路由地址", example = "/user/list")
    private String path;

    @Schema(description = "图标", example = "user")
    private String icon;

    @Schema(description = "组件路径", example = "user/UserList")
    private String component;

    @Schema(description = "组件名", example = "UserList")
    private String componentName;

    @Schema(description = "状态 0开启 1关闭", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "是否可见", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean visible;

    @Schema(description = "是否缓存", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean keepAlive;

    @Schema(description = "是否总是显示", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean alwaysShow;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "子菜单")
    private List<MenuRespVO> children;

}
