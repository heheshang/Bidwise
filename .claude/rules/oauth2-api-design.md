# OAuth2 接口设计规范

## 标准 OAuth2 端点（参照 RFC 6749）

### POST /oauth2/token — 获取令牌
- grant_type=authorization_code 时：需要 code + redirect_uri + state
- grant_type=password 时：需要 username + password + scope
- grant_type=client_credentials 时：需要 scope
- grant_type=refresh_token 时：需要 refresh_token
- client_id + client_secret 通过 HTTP Basic Auth 传递（Authorization header）
- 返回：{ access_token, token_type, expires_in, refresh_token, scope }

### POST /oauth2/authorize — 发起授权（授权码模式）
- 参数：response_type=code, client_id, redirect_uri, scope, state
- 用户已登录 + 已授权 → 302 重定向到 redirect_uri?code=xxx&state=xxx
- 用户已登录 + 未授权 → 返回授权确认页面信息

### POST /oauth2/check-token — 校验令牌
- 参数：token
- 返回：用户信息 + scope + 过期时间

### DELETE /oauth2/token — 注销令牌
- 参数：token
- 同时删除 access_token 和关联的 refresh_token

## 管理接口（后台管理用）
- GET /oauth2/client/page — 客户端分页列表
- GET /oauth2/client/get?id=xxx — 获取客户端详情
- POST /oauth2/client/create — 创建客户端
- PUT /oauth2/client/update — 更新客户端
- DELETE /oauth2/client/delete?id=xxx — 删除客户端
- GET /oauth2/token/page — Token 分页列表
- DELETE /oauth2/token/delete?accessToken=xxx — 强制下线

## VO 设计规范
- 请求 VO 后缀 ReqVO（如 OAuth2ClientSaveReqVO）
- 响应 VO 后缀 RespVO（如 OAuth2ClientRespVO）
- 分页请求 VO 后缀 PageReqVO（如 OAuth2ClientPageReqVO）
- Token 返回 VO 字段名用下划线（access_token 而非 accessToken），符合 OAuth2 规范
