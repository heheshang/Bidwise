# RFC 6749 合规检查清单

## Authorization Code Grant（授权码模式）
- [x] authorization endpoint 独立于 token endpoint
- [x] authorization code 一次性使用
- [x] authorization code 有效期 ≤ 10 分钟（本项目设为 5 分钟）
- [x] redirect_uri 必须与注册时完全匹配
- [x] state 参数透传防 CSRF

## Token Endpoint
- [x] client 认证通过 HTTP Basic Auth
- [x] 支持 grant_type: authorization_code / password / client_credentials / refresh_token
- [x] 返回 token_type = "bearer"
- [x] 返回 expires_in（秒）
- [x] scope 不能超出 client 注册的范围

## Token Revocation（RFC 7009）
- [x] 支持撤销 access_token
- [x] 撤销时同时清除关联的 refresh_token
- [x] 撤销不存在的 token 返回 200（不泄露 token 是否存在）

## Security Considerations
- [x] 所有端点强制 HTTPS（生产环境）
- [x] Token 使用安全随机数生成
- [x] 防止 open redirect 攻击（严格校验 redirect_uri）
