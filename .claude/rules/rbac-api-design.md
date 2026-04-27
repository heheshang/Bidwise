# RBAC 接口设计规范

## 角色管理接口
- POST /permission/role/create — 创建角色
- PUT /permission/role/update — 更新角色
- DELETE /permission/role/delete?id=xxx — 删除角色
- GET /permission/role/get?id=xxx — 获取角色详情
- GET /permission/role/page — 角色分页列表
- GET /permission/role/simple-list — 角色精简列表（下拉框用）
- PUT /permission/role/update-status — 更新角色状态

## 菜单管理接口
- POST /permission/menu/create — 创建菜单
- PUT /permission/menu/update — 更新菜单
- DELETE /permission/menu/delete?id=xxx — 删除菜单
- GET /permission/menu/get?id=xxx — 获取菜单详情
- GET /permission/menu/list — 菜单列表（树形返回）
- GET /permission/menu/simple-list — 菜单精简列表（树形选择器用）

## 权限分配接口
- POST /permission/assign-role-menu — 分配角色菜单
- POST /permission/assign-user-role — 分配用户角色
- POST /permission/assign-role-data-scope — 分配角色数据权限
- GET /permission/role-menu-list?roleId=xxx — 获取角色菜单列表
- GET /permission/user-role-list?userId=xxx — 获取用户角色列表

## VO 设计规范
- 请求 VO 后缀 ReqVO（如 RoleSaveReqVO）
- 响应 VO 后缀 RespVO（如 RoleRespVO）
- 分页请求 VO 后缀 PageReqVO（如 RolePageReqVO）
- 分配请求 VO 前缀 PermissionAssign（如 PermissionAssignRoleMenuReqVO）
