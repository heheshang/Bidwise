# 项目规范 - RBAC 权限管理模块

## 技术栈
- Java 21 + Spring Boot 3.x
- MyBatis-Plus 持久层
- PostgreSQL 15+ + Redis（权限缓存）
- Maven 构建
- MapStruct（对象转换）
- Spring Security（权限注解）

## 架构定位
本模块是系统的**权限管理中心**，参考企业级开源项目的 RBAC 实现。
- 经典 RBAC0 模型：用户 ↔ 角色 ↔ 菜单（权限）
- 菜单三级结构：目录 → 菜单 → 按钮（权限标识）
- 数据权限（DataScope）绑定角色，支持5种范围
- 权限校验走 Redis 缓存，命中率 > 99%

## 包结构（参照企业级规范）
com.example.auth/
├── controller/
│   └── admin/
│       └── permission/            # 角色、菜单、权限管理
│           └── vo/
│               ├── role/          # RoleSaveReqVO, RoleRespVO, RolePageReqVO
│               ├── menu/          # MenuSaveReqVO, MenuRespVO, MenuListReqVO
│               └── permission/    # PermissionAssignRoleMenuReqVO,
│                                  # PermissionAssignUserRoleReqVO,
│                                  # PermissionAssignRoleDataScopeReqVO
├── service/
│   └── permission/                # RoleService, MenuService,
│                                  # PermissionService + 各自 Impl
├── dal/
│   ├── dataobject/permission/     # RoleDO, MenuDO, RoleMenuDO, UserRoleDO
│   ├── postgres/permission/        # 对应 Mapper 接口
│   └── redis/permission/          # RoleMenuRedisCache, UserRoleRedisCache
├── converter/
│   └── permission/                # PermissionConverter（MapStruct）
├── enums/
│   └── permission/                # DataScopeEnum, MenuTypeEnum,
│                                  # RoleCodeEnum, RoleTypeEnum
└── util/
└── permission/                # PermissionHelper（权限工具类）

## 分层规则
- Controller：只做参数校验 + 调用 Service，不超过 15 行
- Service：接口 + Impl 分离，Impl 中注入 Mapper 和 RedisCache
- DAL：Mapper 继承 ExtBaseMapper，RedisCache 单独封装
- Converter：使用 MapStruct，禁止手写 BeanUtils.copyProperties
- VO/DO 严格分离：Controller 只接收 VO，Service 操作 DO，Converter 负责转换

## 命名规范（参照企业级规范）
- DO 后缀：RoleDO, MenuDO, RoleMenuDO, UserRoleDO
- VO 后缀 + 用途：RoleSaveReqVO, RoleRespVO, RolePageReqVO
- Service 命名：XxxService（接口）+ XxxServiceImpl（实现）
- Mapper 命名：XxxMapper
- Converter 命名：PermissionConverter
- RedisCache 命名：XxxRedisCache
- 枚举命名：XxxEnum

## 错误码规范
- 模块编号段：1002030000 ~ 1002030999（RBAC 模块专用）
- 格式：BizError ROLE_NOT_EXISTS = new BizError(1002030000, "角色不存在");
- 每个错误码必须有中文描述

## 运行命令
- 编译：mvn compile
- 测试：mvn test
- 启动：mvn spring-boot:run
