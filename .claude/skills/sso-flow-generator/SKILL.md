---
name: sso-flow-generator
description: 输入SSO场景描述，自动生成授权码模式的完整SSO流程代码（含回调处理）
---

# SSO 单点登录流程生成器

## 输入
- SSO 场景描述（如："A系统跳转到B系统免登录"）
- 客户端配置信息（client_id, redirect_uri, scopes）

## 生成内容
1. OAuth2 客户端注册 SQL（INSERT INTO sys_oauth2_client）
2. SSO 发起方代码（构造授权URL + 重定向）
3. SSO 回调处理代码（接收 code → 换 token → 获取用户信息）
4. Token 校验中间件（Filter / Interceptor）
5. 前端跳转示例代码
6. 完整流程时序图（文字版）

## SSO 流程标准
1. 用户访问 B 系统 → 未登录 → 302 到 A 系统授权端点
2. A 系统检查登录态 → 已登录 → 颁发 code → 302 回 B 系统 callback
3. B 系统用 code 换 access_token（后端调用，不经过浏览器）
4. B 系统用 access_token 获取用户信息
5. B 系统创建本地会话
