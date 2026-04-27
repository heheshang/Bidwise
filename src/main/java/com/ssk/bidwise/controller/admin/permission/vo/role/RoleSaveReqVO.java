package com.ssk.bidwise.controller.admin.permission.vo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 创建/更新角色 Request VO
 *
 * @author Bidwise
 */
@Data
@Schema(description = "创建/更新角色")
public class RoleSaveReqVO {

    @Schema(description = "角色编号", example = "1")
    private Long id;

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 64, message = "角色名称长度不能超过64个字符")
    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "管理员")
    private String name;

    @NotBlank(message = "角色标识不能为空")
    @Size(max = 64, message = "角色标识长度不能超过64个字符")
    @Schema(description = "角色标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    private String code;

    @NotNull(message = "显示顺序不能为空")
    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sort;

    @NotNull(message = "角色类型不能为空")
    @Schema(description = "角色类型 1系统内置 2自定义", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @NotNull(message = "数据范围不能为空")
    @Schema(description = "数据范围 1全部 2自定义部门 3本部门 4本部门及子部门 5仅本人",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer dataScope;

    @Schema(description = "数据范围指定部门编号", example = "[1, 2, 3]")
    private List<Long> dataScopeDeptIds;

    @Size(max = 512, message = "备注长度不能超过512个字符")
    @Schema(description = "备注", example = "管理员")
    private String remark;

    @Schema(description = "状态 0开启 1关闭", example = "0")
    private Integer status;

}
