# OAuth2 表设计规范

## 五张核心表

### sys_oauth2_client（客户端表）
必须字段：
- id BIGINT 主键自增
- client_id VARCHAR(255) 客户端编号（唯一索引）
- secret VARCHAR(255) 客户端密钥（加密存储）
- name VARCHAR(255) 应用名
- logo VARCHAR(512) 应用图标
- description VARCHAR(512) 应用描述
- status TINYINT 状态（0开启 1关闭）
- access_token_validity_seconds INT 访问令牌有效期（秒）
- refresh_token_validity_seconds INT 刷新令牌有效期（秒）
- redirect_uris VARCHAR(2048) 可重定向URI（JSON数组）
- authorized_grant_types VARCHAR(255) 授权类型（JSON数组）
- scopes VARCHAR(255) 授权范围（JSON数组）
- auto_approve_scopes VARCHAR(255) 自动授权范围（JSON数组）
- authorities VARCHAR(255) 权限（JSON数组）
- resource_ids VARCHAR(255) 资源（JSON数组）
- additional_information VARCHAR(4096) 附加信息JSON
- creator/create_time/updater/update_time/deleted 基础字段

### sys_oauth2_access_token（访问令牌表）
必须字段：
- id BIGINT 主键自增
- access_token VARCHAR(255) 访问令牌（唯一索引）
- refresh_token VARCHAR(255) 关联的刷新令牌
- user_id BIGINT 用户编号
- user_type TINYINT 用户类型
- user_info VARCHAR(512) 用户信息JSON
- client_id VARCHAR(255) 客户端编号
- scopes VARCHAR(255) 授权范围JSON
- expires_time DATETIME 过期时间
- tenant_id BIGINT 租户编号

### sys_oauth2_refresh_token（刷新令牌表）
必须字段：
- id BIGINT 主键自增
- refresh_token VARCHAR(255) 刷新令牌（唯一索引）
- user_id BIGINT 用户编号
- user_type TINYINT 用户类型
- client_id VARCHAR(255) 客户端编号
- scopes VARCHAR(255) 授权范围JSON
- expires_time DATETIME 过期时间

### sys_oauth2_code（授权码表）
必须字段：
- id BIGINT 主键自增
- code VARCHAR(255) 授权码（唯一索引）
- user_id BIGINT 用户编号
- user_type TINYINT 用户类型
- client_id VARCHAR(255) 客户端编号
- scopes VARCHAR(255) 授权范围JSON
- redirect_uri VARCHAR(255) 重定向地址
- state VARCHAR(255) 状态参数
- expires_time DATETIME 过期时间

### sys_oauth2_approval（用户授权批准表）
必须字段：
- id BIGINT 主键自增
- user_id BIGINT 用户编号
- user_type TINYINT 用户类型
- client_id VARCHAR(255) 客户端编号
- scope VARCHAR(255) 授权范围（单个）
- approved BOOLEAN 是否批准
- expires_time DATETIME 过期时间

## DO 规范
- 所有 JSON 数组字段用 @TableField(typeHandler = JacksonTypeHandler.class)
- @TableName 的 autoResultMap = true
- 客户端表 DO 加 @SkipTenantFilter（客户端配置跨租户共享）
- 访问令牌表 DO 继承 TenantAuditBaseDO（Token 隔离租户）
- 其他表 DO 继承 AuditBaseDO

## 索引规范
- access_token、refresh_token、code、client_id 必须建唯一索引
- user_id + user_type 建联合索引
- expires_time 建索引（用于定时清理过期数据）
