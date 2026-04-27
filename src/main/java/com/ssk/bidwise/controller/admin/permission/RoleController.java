package com.ssk.bidwise.controller.admin.permission;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.common.vo.PageVO;
import com.ssk.bidwise.controller.admin.permission.vo.role.RolePageReqVO;
import com.ssk.bidwise.controller.admin.permission.vo.role.RoleRespVO;
import com.ssk.bidwise.controller.admin.permission.vo.role.RoleSaveReqVO;
import com.ssk.bidwise.service.permission.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssk.bidwise.common.result.Result.success;

/**
 * 角色管理 Controller
 *
 * @author Bidwise
 */
@Tag(name = "管理后台 - 角色管理")
@RestController
@RequestMapping("/api/v1/permission/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("/create")
    @Operation(summary = "创建角色")
    @PreAuthorize("@perm.check('system:role:create')")
    public Result<Long> create(@Valid @RequestBody RoleSaveReqVO reqVO) {
        return success(roleService.createRole(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新角色")
    @PreAuthorize("@perm.check('system:role:update')")
    public Result<Void> update(@Valid @RequestBody RoleSaveReqVO reqVO) {
        roleService.updateRole(reqVO);
        return success();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除角色")
    @PreAuthorize("@perm.check('system:role:delete')")
    public Result<Void> delete(@RequestParam("id") Long id) {
        roleService.deleteRole(id);
        return success();
    }

    @GetMapping("/get")
    @Operation(summary = "获取角色详情")
    @PreAuthorize("@perm.check('system:role:query')")
    public Result<RoleRespVO> get(@RequestParam("id") Long id) {
        return success(roleService.getRole(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获取角色分页列表")
    @PreAuthorize("@perm.check('system:role:query')")
    public Result<PageVO<RoleRespVO>> page(RolePageReqVO pageReqVO) {
        return success(roleService.getRolePage(pageReqVO));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获取角色精简列表，用于下拉框")
    public Result<List<RoleRespVO>> simpleList() {
        return success(roleService.getRoleSimpleList());
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新角色状态")
    @PreAuthorize("@perm.check('system:role:update')")
    public Result<Void> updateStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        roleService.updateRoleStatus(id, status);
        return success();
    }

}
