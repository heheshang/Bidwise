# 数据权限（DataScope）规范

## 五种数据范围
| 枚举值 | 名称 | 含义 |
|---|---|---|
| 1 | ALL | 查看全部数据 |
| 2 | DEPT_CUSTOM | 查看指定部门的数据 |
| 3 | DEPT_ONLY | 仅看本部门数据 |
| 4 | DEPT_AND_CHILD | 本部门及下级部门数据 |
| 5 | SELF | 仅看自己的数据 |

## 实现机制
- 数据权限绑定在**角色**上（RoleDO.dataScope + dataScopeDeptIds）
- 一个用户多个角色时，取**并集**（最大权限原则）
- 通过 PermissionService.getDeptDataPermission(userId) 汇总

## 汇总逻辑
1. 获取用户所有启用角色
2. 遍历每个角色的 dataScope：
   - 遇到 ALL → 直接返回"全部可见"
   - 遇到 DEPT_CUSTOM → 收集 dataScopeDeptIds
   - 遇到 DEPT_ONLY → 收集用户所在部门
   - 遇到 DEPT_AND_CHILD → 收集用户所在部门 + 所有子部门
   - 遇到 SELF → 标记"包含本人数据"
3. 各部门 ID 取并集
4. 返回 DeptDataPermissionRespDTO

## SQL 拦截实现
- 自定义 MyBatis Interceptor 拦截 SELECT 语句
- 根据 DeptDataPermissionRespDTO 自动拼接 WHERE 条件：
  - ALL → 不追加条件
  - 有部门 → AND dept_id IN (...)
  - SELF → AND creator = #{userId}
  - 部门 + SELF → AND (dept_id IN (...) OR creator = #{userId})

## 使用注解
- @DataScope 标注在 Mapper 方法上，声明需要数据权限过滤
- 指定 deptColumn（部门字段名）和 userColumn（用户字段名）
