# RBAC 表设计规范

## 四张核心表

### sys_role（角色表）
必须字段：
- id BIGINT 主键自增
- name VARCHAR(64) 角色名称
- code VARCHAR(64) 角色标识（唯一索引，如 super_admin, tenant_admin）
- sort INT 显示顺序
- status TINYINT 状态（0开启 1关闭）
- type TINYINT 角色类型（1=内置SYSTEM 2=自定义CUSTOM）
- data_scope TINYINT 数据范围（1=全部 2=自定义部门 3=本部门 4=本部门及子部门 5=仅本人）
- data_scope_dept_ids VARCHAR(2048) 数据范围指定部门（JSON数组，data_scope=2时使用）
- remark VARCHAR(512) 备注
- tenant_id BIGINT 租户编号
- creator/create_time/updater/update_time/deleted 基础字段

### sys_menu（菜单表，全局共享）
必须字段：
- id BIGINT 主键自增
- name VARCHAR(64) 菜单名称
- permission VARCHAR(128) 权限标识（如 system:role:create）
- type TINYINT 菜单类型（1=目录 2=菜单 3=按钮）
- sort INT 显示顺序
- parent_id BIGINT 父菜单ID（0=根节点）
- path VARCHAR(256) 路由地址
- icon VARCHAR(128) 菜单图标
- component VARCHAR(256) 组件路径
- component_name VARCHAR(64) 组件名
- status TINYINT 状态
- visible BOOLEAN 是否可见
- keep_alive BOOLEAN 是否缓存（Vue keep-alive）
- always_show BOOLEAN 是否总是显示
- creator/create_time/updater/update_time/deleted 基础字段

### sys_role_menu（角色菜单关联表）
必须字段：
- id BIGINT 主键自增
- role_id BIGINT 角色编号
- menu_id BIGINT 菜单编号
- tenant_id BIGINT 租户编号
- creator/create_time/updater/update_time/deleted 基础字段

### sys_user_role（用户角色关联表）
必须字段：
- id BIGINT 主键自增
- user_id BIGINT 用户编号
- role_id BIGINT 角色编号
- creator/create_time/updater/update_time/deleted 基础字段

## DO 规范
- RoleDO 继承 TenantAuditBaseDO（角色租户隔离）
- MenuDO 继承 AuditBaseDO + 标注 @SkipTenantFilter（菜单全局共享）
- RoleMenuDO 继承 TenantAuditBaseDO（关联也租户隔离）
- UserRoleDO 继承 AuditBaseDO（通过角色间接隔离）
- JSON 数组字段用 @TableField(typeHandler = JacksonTypeHandler.class)
- @TableName 的 autoResultMap = true

## 索引规范
- role.code 唯一索引
- menu.permission 索引
- role_menu 的 (role_id, menu_id) 联合唯一索引
- user_role 的 (user_id, role_id) 联合唯一索引
- tenant_id 建索引
