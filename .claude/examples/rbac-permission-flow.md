# RBAC 权限校验完整链路

## 权限判定时序

用户请求 → Spring Security Filter → @PreAuthorize("@perm.check('system:user:list')")
→ PermissionService.hasAnyPermissions(userId, "system:user:list")
  → 查缓存获取用户角色列表
    → 过滤掉禁用状态的角色
      → 判断是否包含超级管理员 → 是 → 直接放行
      → 否 → 查缓存获取权限标识对应的菜单ID列表
        → 查缓存获取菜单对应的角色ID列表
          → 与用户角色取交集
            → 交集非空 → 放行
            → 交集为空 → 拒绝（403）

## 代码调用链

### 1. 权限校验
PermissionServiceImpl.hasAnyPermissions(userId, permissions)
  → getUserRoleIdListFromCache(userId)         // 用户→角色（Redis缓存）
    → roleService.getRoleListFromCache(roleIds) // 角色详情（过滤禁用）
      → roleService.hasAnySuperAdmin(roleIds)   // 超管判断
      → menuService.getMenuIdListByPermissionFromCache(permission) // 权限→菜单
        → getMenuRoleIdListFromCache(menuId)     // 菜单→角色（Redis缓存）
          → CollUtil.containsAny(userRoleIds, menuRoleIds) // 取交集

### 2. 分配角色菜单
PermissionController.assignRoleMenu(reqVO)
  → permissionService.assignRoleMenu(roleId, menuIds)
    → validateRoleExists(roleId)                  // 校验角色存在
    → validateMenuExists(menuIds)                  // 校验菜单存在且启用
    → roleMenuMapper.deleteByRoleId(roleId)        // 删除旧关联
    → roleMenuMapper.insertBatch(newRoleMenuList)  // 批量插入新关联
    → roleMenuRedisCache.deleteByRoleId(roleId)    // 清除缓存

### 3. 数据权限获取
PermissionServiceImpl.getDeptDataPermission(userId)
  → getUserRoleIdListFromCache(userId)             // 获取用户角色
  → roleService.getRoleListFromCache(roleIds)       // 获取角色详情
  → 遍历角色的 dataScope：
      ALL → result.setAll(true), return
      DEPT_CUSTOM → result.getDeptIds().addAll(dataScopeDeptIds)
      DEPT_ONLY → result.getDeptIds().add(userDeptId)
      DEPT_AND_CHILD → result.getDeptIds().addAll(deptService.getChildDeptIds(userDeptId))
      SELF → result.setSelf(true)
  → return result（各部门ID取并集）
