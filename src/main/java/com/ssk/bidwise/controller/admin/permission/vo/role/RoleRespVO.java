package com.ssk.bidwise.model.vo.permission.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色 Response VO
 *
 * @author Bidwise
 */
@Data
@Schema(description = "角色信息")
public class RoleRespVO {

    @Schema(description = "角色编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "管理员")
    private String name;

    @Schema(description = "角色标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    private String code;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sort;

    @Schema(description = "角色类型 1系统内置 2自定义", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "数据范围", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer dataScope;

    @Schema(description = "数据范围指定部门编号", example = "[1, 2, 3]")
    private List<Long> dataScopeDeptIds;

    @Schema(description = "备注", example = "管理员")
    private String remark;

    @Schema(description = "状态 0开启 1关闭", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "租户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Long tenantId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
