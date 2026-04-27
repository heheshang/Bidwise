---
name: crud-generator
description: 自动生成标准 CRUD 全栈代码，遵循项目分层规范
---

# CRUD Generator Skill

自动生成标准 CRUD 全栈代码，遵循项目统一分层规范。

## 触发词

`生成CRUD`, `crud生成`, `创建CRUD`, `新增模块`, `生成代码` 触发此技能。

## 输入
- 表名（复数，小写下划线，如：bids）
- 模块分组（system/oauth2/permission/biz）
- 字段列表（类型 + 是否必填 + 约束）

## 生成文件（参照企业级目录结构）
1. `dal/dataobject/{module}/{Entity}DO.java` — 数据对象 DO
2. `dal/mysql/{module}/{Entity}Mapper.java` — Mapper（继承 BaseMapper）
3. `model/dto/Create{Entity}Request.java` — 创建请求 DTO
4. `model/dto/Update{Entity}Request.java` — 更新请求 DTO
5. `model/vo/{Entity}VO.java` — 响应 VO
6. `model/converter/{Entity}Converter.java` — 对象转换器
7. `service/{Entity}Service.java` — 业务接口
8. `service/impl/{Entity}ServiceImpl.java` — 业务实现
9. `controller/{Entity}Controller.java` — 控制器
10. `src/test/java/com/ssk/bidwise/{Entity}ServiceImplTest.java` — 单元测试

## 规范要求
- 严格遵守 CLAUDE.md 和所有 `.claude/rules/` 中的规范
- DO 必须包含四个基础字段：`id`, `createTime`, `updateTime`, `isDeleted`
- 使用 `@TableName` 指定表名，`@TableId(type = IdType.AUTO)` 主键
- DTO 所有非空字段添加 `@NotNull`/`@NotBlank` 校验注解（带 message）
- VO 不返回 `isDeleted` 字段
- Mapper 继承 `BaseMapper<{Entity}DO>`，无多余方法
- Service 接口定义：`pageQuery`, `getDetail`, `create`, `update`, `delete`
- Service 实现：分页按 `createTime` 降序，不存在抛 `BusinessException`
- 写方法加 `@Transactional(rollbackFor = Exception.class)`
- Controller 标准 REST 路径：`/api/v1/{table}`，所有返回 `Result<T>`
- 单元测试：每个 public 方法覆盖 正向测试 + 异常测试，遵循 Given-When-Then
- 使用 `@Mock` 隔离 Mapper 依赖，不依赖真实数据库
