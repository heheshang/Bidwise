---
name: data-scope-generator
description: 输入业务模块名，自动生成带数据权限过滤的查询代码
---

# 数据权限代码生成器

## 输入
- 业务模块名（如：Order、Customer）
- 部门字段名（默认 dept_id）
- 创建人字段名（默认 creator）

## 生成内容
1. @DataScope 注解的使用示例
2. Mapper 查询方法（带数据权限过滤）
3. MyBatis XML 中的 SQL 片段
4. Service 中获取数据权限并传递的代码
5. 测试用例（覆盖 5 种 DataScope）

## 数据权限 SQL 拼接规则
- ALL：不追加条件
- DEPT_CUSTOM：AND t.dept_id IN (#{deptIds})
- DEPT_ONLY：AND t.dept_id = #{userDeptId}
- DEPT_AND_CHILD：AND t.dept_id IN (SELECT id FROM sys_dept WHERE id = #{userDeptId} OR find_in_set(#{userDeptId}, ancestors))
- SELF：AND t.creator = #{userId}
