package com.ssk.bidwise.model.vo.permission.assign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 角色菜单列表 Response VO
 *
 * @author Bidwise
 */
@Data
@Schema(description = "角色已分配菜单列表")
public class PermissionRoleMenuListRespVO {

    @Schema(description = "所有菜单树形列表")
    private List<?> menus;

    @Schema(description = "该角色已分配的菜单编号列表")
    private List<Long> selectedMenuIds;

}
