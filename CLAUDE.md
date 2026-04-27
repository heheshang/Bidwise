# CLAUDE.md

> Bidwise - AI 驱动竞价系统项目规范

## 🚀 常用命令

| 操作 | 命令 |
|------|------|
| 构建 | `./mvnw clean package` |
| 运行 | `./mvnw spring-boot:run` |
| 测试所有 | `./mvnw test` |
| 测试单个 | `./mvnw test -Dtest=ClassName` |
| 仅编译 | `./mvnw compile` |

---

## ⚙️ 配置

- DashScope API 密钥：环境变量 `DASHSCOPE_API_KEY` → `application.yaml`

---

## 📋 全局规范

### 技术栈

| 技术 | 要求 |
|------|------|
| Java | 21+ |
| Spring Boot | 3.x |
| MyBatis-Plus | 3.5.x |
| PostgreSQL | 15+ |
| Redis | 缓存 |
| MapStruct | 对象转换 |
| Lombok | 代码简化 |
| Spring AI | AI 集成 |
| Spring Security | 权限注解 |

### 代码规则

- **Controller**：≤ 15 行，只做参数校验 + 调用 Service
- **Service**：≤ 50 行，每个方法只做一件事
- **返回**：所有 API 必须返回 `Result<T>`
- **禁止**：Controller 写 SQL 或直接调用 Mapper
- **注释**：public 方法必须有 Javadoc

### API 设计

- RESTful：`GET` 查询、`POST` 新增、`PUT` 修改、`DELETE` 删除
- 前缀：`/api/v1/{module}`
- 分页：`page` + `size` 参数

---

## 🏗️ 整体目录结构

```
com.ssk.bidwise/
├── controller/admin/
│   ├── auth/vo/          # AuthLoginReqVO/AuthLoginRespVO
│   ├── oauth2/vo/        # OAuth2 开放接口 VO
│   └── permission/vo/    # RBAC 角色/菜单/权限 VO
├── service/
│   ├── auth/impl/        # SysAuthService + Impl
│   ├── oauth2/           # OAuth2*Service + 各自 Impl
│   ├── permission/       # RoleService/MenuService/PermissionService + Impl
│   └── ai/impl/          # Spring AI 服务
├── dal/
│   ├── dataobject/
│   │   ├── oauth2/       # OAuth2*DO
│   │   └── permission/   # RoleDO/MenuDO/RoleMenuDO/UserRoleDO
│   ├── mysql/
│   │   ├── oauth2/       # OAuth2*Mapper
│   │   └── permission/   # RoleMapper/MenuMapper
│   └── redis/
│       ├── oauth2/       # OAuth2*RedisCache
│       └── permission/   # RoleMenuRedisCache/UserRoleRedisCache
├── converter/
│   ├── auth/             # AuthConverter (MapStruct)
│   ├── oauth2/           # OAuth2OpenConverter
│   └── permission/       # PermissionConverter
├── enums/
│   ├── oauth2/           # OAuth2GrantTypeEnum
│   └── permission/       # DataScopeEnum/MenuTypeEnum
├── util/
│   ├── oauth2/           # OAuth2Helper
│   └── permission/       # PermissionHelper
├── mapper/               # 通用 Mapper
├── model/                # 通用 entity/dto/vo
├── config/               # 配置类
├── common/               # result/exception/constant/util
└── aspect/               # AOP 切面
```

### 分层职责

| 层级 | 职责 |
|------|------|
| Controller | 参数校验 → 调用 Service → 返回 `Result<T>` |
| Service | 业务逻辑 + 事务控制，写方法加 `@Transactional` |
| Mapper | CRUD 操作，继承 `BaseMapper<T>` |
| Model | entity 对应表，dto 入参，vo 出参 |

**依赖规则**：`Controller → Service → Mapper`，禁止反向/跨层调用

---

## 🔐 OAuth2 认证授权模块

**特性**：自实现 OAuth2 Server，支持 4 种模式，Token 双存储 (MySQL + Redis)，SSO 基于 authorization_code

**命名规范**：

| 类型 | 示例 |
|------|------|
| DO | `OAuth2ClientDO` |
| Req VO | `OAuth2ClientSaveReqVO` |
| Resp VO | `OAuth2ClientRespVO` |
| Service | `OAuth2XxxService` + `OAuth2XxxServiceImpl` |
| Mapper | `OAuth2XxxMapper` |
| Converter | `AuthConverter` |
| ErrorCode | `1002020000 ~ 1002020999` (OAuth2 专用) |

---

## 🔑 RBAC 权限管理模块

**特性**：经典 RBAC 模型 用户 ↔ 角色 ↔ 菜单，菜单三级结构（目录→菜单→按钮），5种数据权限范围，Redis 权限缓存

**命名规范**：

| 类型 | 示例 |
|------|------|
| DO | `RoleDO`, `MenuDO`, `RoleMenuDO`, `UserRoleDO` |
| Req VO | `RoleSaveReqVO`, `RolePageReqVO` |
| Resp VO | `RoleRespVO`, `MenuRespVO` |
| Service | `XxxService` + `XxxServiceImpl` |
| Mapper | `XxxMapper` |
| Converter | `PermissionConverter` |
| RedisCache | `XxxRedisCache` |
| ErrorCode | `1002030000 ~ 1002030999` (RBAC 专用) |

---

## 🤖 Spring AI 模块

**规范**：
- API 密钥环境变量注入，不硬编码
- AI 请求失败要有重试降级
- Prompt 模板建议外部化
- 路径前缀 `/api/v1/ai/xxx`
- 流式响应支持 SSE
