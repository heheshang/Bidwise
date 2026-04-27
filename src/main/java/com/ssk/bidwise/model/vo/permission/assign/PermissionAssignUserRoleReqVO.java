package com.ssk.bidwise.model.vo.permission.assign;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 分配用户角色 Request VO
 *
 * @author Bidwise
 */
@Data
@Schema(description = "分配用户角色")
public class PermissionAssignUserRoleReqVO {

    @NotNull(message = "用户编号不能为空")
    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "角色编号列表", example = "[1, 2, 3]")
    private List<Long> roleIds;

}
