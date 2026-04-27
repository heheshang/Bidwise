# Token 生命周期管理规范

## 创建流程
1. 生成 access_token（UUID）
2. 生成 refresh_token（UUID）
3. 写入 MySQL（持久化）
4. 写入 Redis（缓存，TTL = expiresTime - now）
5. 返回 access_token + refresh_token + expires_in

## 校验流程
1. 先查 Redis（命中率 > 99%）
2. Redis 未命中 → 查 MySQL
3. 检查 expiresTime > now
4. 检查对应的 client 是否有效（status = 0）

## 刷新流程
1. 用 refresh_token 查 MySQL
2. 校验 refresh_token 未过期
3. 删除旧的 access_token（MySQL + Redis）
4. 生成新的 access_token
5. 写入 MySQL + Redis
6. 返回新 access_token（refresh_token 不变）

## 注销流程
1. 删除 access_token（MySQL + Redis）
2. 删除关联的 refresh_token（MySQL）
3. 记录登出日志

## 定期清理
- 过期 access_token：每小时清理一次
- 过期 refresh_token：每天清理一次
- 过期 authorization_code：每小时清理一次（code 有效期 5 分钟）
