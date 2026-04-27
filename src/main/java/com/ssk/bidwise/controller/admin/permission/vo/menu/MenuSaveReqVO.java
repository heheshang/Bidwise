package com.ssk.bidwise.controller.admin.permission.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建/更新菜单 Request VO
 *
 * @author Bidwise
 */
@Data
@Schema(description = "创建/更新菜单")
public class MenuSaveReqVO {

    @Schema(description = "菜单编号", example = "1")
    private Long id;

    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 64, message = "菜单名称长度不能超过64个字符")
    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "用户管理")
    private String name;

    @Size(max = 128, message = "权限标识长度不能超过128个字符")
    @Schema(description = "权限标识", example = "system:user:create")
    private String permission;

    @NotNull(message = "菜单类型不能为空")
    @Schema(description = "菜单类型 1目录 2菜单 3按钮", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @NotNull(message = "显示顺序不能为空")
    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sort;

    @Schema(description = "父菜单ID", example = "0")
    private Long parentId;

    @Size(max = 256, message = "路由地址长度不能超过256个字符")
    @Schema(description = "路由地址", example = "/user/list")
    private String path;

    @Size(max = 128, message = "图标长度不能超过128个字符")
    @Schema(description = "图标", example = "user")
    private String icon;

    @Size(max = 256, message = "组件路径长度不能超过256个字符")
    @Schema(description = "组件路径", example = "user/UserList")
    private String component;

    @Size(max = 64, message = "组件名长度不能超过64个字符")
    @Schema(description = "组件名", example = "UserList")
    private String componentName;

    @Schema(description = "状态 0开启 1关闭", example = "0")
    private Integer status;

    @Schema(description = "是否可见", example = "true")
    private Boolean visible;

    @Schema(description = "是否缓存", example = "false")
    private Boolean keepAlive;

    @Schema(description = "是否总是显示", example = "false")
    private Boolean alwaysShow;

}
