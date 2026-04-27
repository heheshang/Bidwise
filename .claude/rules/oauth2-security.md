# OAuth2 安全红线

## 绝对禁止（违反 = 不通过 review）
- ❌ client_secret 禁止明文存储，必须加密（BCrypt）
- ❌ access_token 禁止放在 URL 参数中，必须放 Header（Bearer）
- ❌ 禁止 Token 永不过期，access_token 最长 30 分钟，refresh_token 最长 30 天
- ❌ 禁止不校验 redirect_uri，必须与注册时一致
- ❌ 禁止 authorization_code 重复使用，用过即删
- ❌ 禁止在日志中打印 token / secret / password 的完整值
- ❌ 禁止 CORS 配置 allow-origin: *（生产环境）

## 必须实现
- ✅ Token 生成使用 UUID + 安全随机数，禁止可预测的序列
- ✅ 密码登录必须有失败次数限制（5次锁定30分钟）
- ✅ 所有 Token 操作记录审计日志
- ✅ client_credentials 模式颁发的 Token 必须限制 scope
- ✅ 刷新 Token 时，旧的 access_token 必须立即失效

## Redis 缓存安全
- Token 存 Redis 时，TTL 必须与 expiresTime 一致，禁止设为 -1
- 删除 Token 时必须同时删 PostgreSQL 和 Redis
- 清理多余字段（updater, updateTime, creator, createTime, deleted）后再缓存
