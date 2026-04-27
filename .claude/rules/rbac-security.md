# RBAC 安全红线

## 绝对禁止（违反 = 不通过 review）
- ❌ 禁止在前端做权限校验后不做后端校验（前端校验可绕过）
- ❌ 禁止硬编码角色判断（if role == "admin"），必须用权限标识
- ❌ 禁止越权操作：普通角色不能修改/删除超级管理员
- ❌ 禁止内置角色（SYSTEM类型）被删除或修改 code
- ❌ 禁止菜单权限标识重复（permission 字段必须唯一）
- ❌ 禁止数据权限绕过：所有列表查询必须经过 DataScope 过滤
- ❌ 禁止在日志中打印完整的权限列表（数据量大 + 信息泄露）

## 必须实现
- ✅ 超级管理员（super_admin）拥有所有权限，代码中直接放行
- ✅ 角色禁用后，该角色下所有用户的权限立即失效（清缓存）
- ✅ 菜单删除时级联清理 role_menu 关联（processMenuDeleted）
- ✅ 角色删除时级联清理 role_menu + user_role 关联（processRoleDeleted）
- ✅ 用户删除时级联清理 user_role 关联（processUserDeleted）
- ✅ 权限变更后必须清除相关 Redis 缓存
- ✅ 分配角色菜单时，菜单 ID 必须校验存在且启用

## 缓存安全
- 权限缓存变更时必须同时清 Redis
- 缓存 key 必须包含业务标识前缀，防止 key 冲突
- 不缓存完整 DO 对象，只缓存 ID 映射关系
