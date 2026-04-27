---
name: rbac-module-generator
description: 输入RBAC相关实体名，自动生成 DO + Mapper + RedisCache + Service + Controller + VO + Converter + DDL 全套代码
---

# RBAC 模块代码生成器

## 输入
- 实体名（如：Role、Menu）
- 核心字段列表

## 生成文件（参照企业级目录结构）
1. dal/dataobject/permission/XxxDO.java — 数据对象
2. dal/mysql/permission/XxxMapper.java — Mapper（继承 ExtBaseMapper）
3. dal/redis/permission/XxxRedisCache.java — Redis缓存层（如需要）
4. service/permission/XxxService.java — 业务接口（含完整 Javadoc）
5. service/permission/XxxServiceImpl.java — 业务实现
6. controller/admin/permission/vo/xxx/XxxSaveReqVO.java — 创建/更新请求
7. controller/admin/permission/vo/xxx/XxxRespVO.java — 响应
8. controller/admin/permission/vo/xxx/XxxPageReqVO.java — 分页请求（如需要）
9. controller/admin/permission/XxxController.java — 控制器
10. converter/permission/XxxConverter.java — MapStruct 转换器
11. src/main/resources/db/migration/Vx__create_xxx.sql — DDL

## 规范要求
- 严格遵守 CLAUDE.md 和所有 rules/ 中的规范
- DO 中 JSON 字段用 @TableField(typeHandler = JacksonTypeHandler.class)
- Mapper 继承 ExtBaseMapper，查询用 LambdaQueryChain
- Service 接口每个方法必须有 Javadoc
- Controller 用 @Tag + @Operation 标注
- VO 中用 @NotNull / @NotBlank / @Size 等 JSR 380 注解
- Converter 用 MapStruct @Mapper + INSTANCE 单例模式
