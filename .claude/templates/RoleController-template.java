// .claude/templates/RoleController-template.java
@Tag(name = "管理后台 - 角色管理")
@RestController
@RequestMapping("/permission/role")
@Validated
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("/create")
    @Operation(summary = "创建角色")
    @PreAuthorize("@perm.check('system:role:create')")
    public ApiResult<Long> createRole(@Valid @RequestBody RoleSaveReqVO reqVO) {
        return success(roleService.createRole(reqVO, RoleTypeEnum.CUSTOM));
    }

    @PutMapping("/update")
    @Operation(summary = "更新角色")
    @PreAuthorize("@perm.check('system:role:update')")
    public ApiResult<Boolean> updateRole(@Valid @RequestBody RoleSaveReqVO reqVO) {
        roleService.updateRole(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除角色")
    @PreAuthorize("@perm.check('system:role:delete')")
    public ApiResult<Boolean> deleteRole(@RequestParam("id") Long id) {
        roleService.deleteRole(id);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获取角色分页")
    @PreAuthorize("@perm.check('system:role:query')")
    public ApiResult<PageData<RoleRespVO>> getRolePage(@Valid RolePageReqVO reqVO) {
        PageData<RoleDO> pageData = roleService.getRolePage(reqVO);
        return success(PermissionConverter.INSTANCE.toRolePage(pageData));
    }
}
