package com.ssk.bidwise.model.vo.permission.assign;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 分配角色菜单 Request VO
 *
 * @author Bidwise
 */
@Data
@Schema(description = "分配角色菜单")
public class PermissionAssignRoleMenuReqVO {

    @NotNull(message = "角色编号不能为空")
    @Schema(description = "角色编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long roleId;

    @Schema(description = "菜单编号列表", example = "[1, 2, 3]")
    private List<Long> menuIds;

}
