# OAuth2 授权码模式完整流程

## 时序图（文字版）

用户 → B系统前端 → A系统授权端点 → A系统登录页 → A系统授权确认
→ A系统生成code → 302回B系统callback → B系统后端用code换token
→ B系统拿token调A系统/oauth2/user/get → 获取用户信息 → 建立本地会话

## 代码调用链

### 1. 获取授权码
OAuth2EndpointController.authorize()
  → oauth2ClientService.validClientFromCache() // 校验客户端
  → oauth2ApprovalService.checkPreApproval() // 检查是否已授权
  → oauth2GrantService.grantCodeForAuthCode() // 生成 code
    → oauth2CodeService.generateAuthCode() // 存库
  → OAuth2Helper.buildRedirectUrl() // 拼接回调URL

### 2. 用 code 换 token
OAuth2EndpointController.postAccessToken(grant_type=authorization_code)
  → oauth2ClientService.validClientFromCache() // 校验客户端+密钥
  → oauth2GrantService.grantCodeForToken()
    → oauth2CodeService.redeemAuthCode() // 消费 code（用后即删）
    → oauth2TokenService.createAccessToken() // 生成 token
      → 写 MySQL
      → 写 Redis（oauth2AccessTokenRedisCache.set()）

### 3. 校验 token
OAuth2EndpointController.checkToken()
  → oauth2TokenService.checkAccessToken()
    → oauth2AccessTokenRedisCache.get() // 先查 Redis
    → 或 oauth2AccessTokenMapper.findByAccessToken() // Redis 未命中查 MySQL
