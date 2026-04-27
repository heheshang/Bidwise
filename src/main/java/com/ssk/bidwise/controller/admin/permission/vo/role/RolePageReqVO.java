package com.ssk.bidwise.controller.admin.permission.vo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 角色分页 Request VO
 *
 * @author Bidwise
 */
@Data
@Schema(description = "角色分页查询")
public class RolePageReqVO {

    @Schema(description = "角色名称，模糊匹配", example = "管理员")
    private String name;

    @Schema(description = "角色标识，模糊匹配", example = "admin")
    private String code;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

    @Schema(description = "页码，从 1 开始", example = "1")
    private Integer page = 1;

    @Schema(description = "每页条数，最大值为 100", example = "10")
    private Integer size = 10;

}
