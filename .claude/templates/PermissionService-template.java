// .claude/templates/PermissionService-template.java
public interface PermissionService {

    /**
     * 判断用户是否拥有指定权限中的任意一个
     *
     * @param userId 用户编号
     * @param permissions 权限标识数组
     * @return 是否拥有
     */
    boolean hasAnyPermissions(Long userId, String... permissions);

    /**
     * 判断用户是否拥有指定角色中的任意一个
     *
     * @param userId 用户编号
     * @param roles 角色标识数组
     * @return 是否拥有
     */
    boolean hasAnyRoles(Long userId, String... roles);

    /**
     * 设置角色菜单
     *
     * @param roleId 角色编号
     * @param menuIds 菜单编号集合
     */
    void assignRoleMenu(Long roleId, Set<Long> menuIds);

    /**
     * 设置用户角色
     *
     * @param userId 用户编号
     * @param roleIds 角色编号集合
     */
    void assignUserRole(Long userId, Set<Long> roleIds);

    /**
     * 设置角色数据权限
     *
     * @param roleId 角色编号
     * @param dataScope 数据范围
     * @param dataScopeDeptIds 部门编号集合
     */
    void assignRoleDataScope(Long roleId, Integer dataScope, Set<Long> dataScopeDeptIds);

    /**
     * 获取用户的部门数据权限
     *
     * @param userId 用户编号
     * @return 部门数据权限
     */
    DeptDataPermissionRespDTO getDeptDataPermission(Long userId);

    /**
     * 处理角色删除时的级联清理
     *
     * @param roleId 角色编号
     */
    void processRoleDeleted(Long roleId);

    /**
     * 处理菜单删除时的级联清理
     *
     * @param menuId 菜单编号
     */
    void processMenuDeleted(Long menuId);

    /**
     * 处理用户删除时的级联清理
     *
     * @param userId 用户编号
     */
    void processUserDeleted(Long userId);
}
