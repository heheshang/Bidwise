package com.ssk.bidwise.controller.admin.permission;

import com.ssk.bidwise.common.result.Result;
import com.ssk.bidwise.model.vo.permission.assign.PermissionAssignRoleMenuReqVO;
import com.ssk.bidwise.model.vo.permission.assign.PermissionAssignUserRoleReqVO;
import com.ssk.bidwise.model.vo.permission.assign.PermissionRoleMenuListRespVO;
import com.ssk.bidwise.service.permission.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssk.bidwise.common.result.Result.success;

/**
 * 权限分配 Controller
 *
 * @author Bidwise
 */
@Tag(name = "管理后台 - 权限分配")
@RestController
@RequestMapping("/api/v1/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @PostMapping("/assign-role-menu")
    @Operation(summary = "分配角色菜单")
    @PreAuthorize("@perm.check('system:permission:assign-role-menu')")
    public Result<Void> assignRoleMenu(@Valid @RequestBody PermissionAssignRoleMenuReqVO reqVO) {
        permissionService.assignRoleMenu(reqVO);
        return success();
    }

    @GetMapping("/role-menu-list")
    @Operation(summary = "获取角色菜单列表（所有菜单 + 已选中）")
    @PreAuthorize("@perm.check('system:permission:assign-role-menu')")
    public Result<PermissionRoleMenuListRespVO> getRoleMenuList(@RequestParam("roleId") Long roleId) {
        return success(permissionService.getRoleMenuList(roleId));
    }

    @PostMapping("/assign-user-role")
    @Operation(summary = "分配用户角色")
    @PreAuthorize("@perm.check('system:permission:assign-user-role')")
    public Result<Void> assignUserRole(@Valid @RequestBody PermissionAssignUserRoleReqVO reqVO) {
        permissionService.assignUserRole(reqVO);
        return success();
    }

    @GetMapping("/user-role-list")
    @Operation(summary = "获取用户已分配角色编号列表")
    public Result<List<Long>> getUserRoleList(@RequestParam("userId") Long userId) {
        return success(permissionService.getUserRoleIdList(userId));
    }

}
