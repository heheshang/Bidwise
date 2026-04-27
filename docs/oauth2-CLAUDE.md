# 项目规范 - OAuth2 / SSO 单点登录模块

## 技术栈
- Java 21 + Spring Boot 3.x
- MyBatis-Plus 持久层
- MySQL 8.0 + Redis（Token缓存）
- Maven 构建
- MapStruct（对象转换）

## 架构定位
本模块是系统的**认证授权中心**，参考企业级开源项目的 OAuth2 实现。
- 自实现 OAuth2 Server（不依赖 Spring Authorization Server）
- 支持四种授权模式：authorization_code / password / client_credentials / refresh_token
- Token 双存储：MySQL 持久化 + Redis 缓存（热数据）
- SSO 单点登录基于 authorization_code 模式实现

## 包结构（参照企业级规范）
com.example.auth/
├── controller/
│   └── admin/
│       ├── auth/              # 登录认证（密码登录、短信登录、社交登录）
│       │   └── vo/            # AuthLoginReqVO, AuthLoginRespVO
│       └── oauth2/            # OAuth2 开放接口
│           └── vo/
│               ├── client/    # OAuth2ClientSaveReqVO, OAuth2ClientRespVO
│               ├── open/      # OAuth2OpenAccessTokenRespVO
│               ├── token/     # OAuth2AccessTokenRespVO
│               └── user/      # OAuth2UserInfoRespVO
├── service/
│   ├── auth/                  # SysAuthService + Impl
│   └── oauth2/                # OAuth2ClientService, OAuth2TokenService,
│                              # OAuth2GrantService, OAuth2CodeService,
│                              # OAuth2ApprovalService + 各自 Impl
├── dal/
│   ├── dataobject/oauth2/     # OAuth2ClientDO, OAuth2AccessTokenDO,
│   │                          # OAuth2RefreshTokenDO, OAuth2CodeDO, OAuth2ApprovalDO
│   ├── mysql/oauth2/          # 对应 Mapper 接口
│   └── redis/oauth2/          # OAuth2AccessTokenRedisCache（Redis缓存层）
├── converter/
│   ├── auth/                  # AuthConverterer（MapStruct）
│   └── oauth2/                # OAuth2OpenConverterer（MapStruct）
├── enums/
│   └── oauth2/                # OAuth2GrantTypeEnum, OAuth2Consts
└── util/
    └── oauth2/                # OAuth2Helper（HttpServletRequest 解析工具）

## 分层规则
- Controller：只做参数校验 + 调用 Service，不超过 15 行
- Service：接口 + Impl 分离，Impl 中注入 Mapper 和 RedisCache
- DAL：Mapper 继承 ExtBaseMapper，RedisCache 单独封装
- Converter：使用 MapStruct，禁止手写 BeanUtils.copyProperties
- VO/DO 严格分离：Controller 只接收 VO，Service 操作 DO，Converter 负责转换

## 命名规范（参照企业级规范）
- DO 后缀：OAuth2ClientDO, OAuth2AccessTokenDO
- VO 后缀 + 用途：OAuth2ClientSaveReqVO, OAuth2ClientRespVO, OAuth2ClientPageReqVO
- Service 命名：OAuth2XxxService（接口）+ OAuth2XxxServiceImpl（实现）
- Mapper 命名：OAuth2XxxMapper
- Converterer 命名：OAuth2OpenConverterer, AuthConverterer
- RedisCache 命名：OAuth2AccessTokenRedisCache
- 枚举命名：OAuth2GrantTypeEnum

## 错误码规范
- 模块编号段：1002020000 ~ 1002020999（OAuth2 模块专用）
- 格式：BizError OAUTH2_CLIENT_NOT_EXISTS = new BizError(1002020000, "OAuth2 客户端不存在");
- 每个错误码必须有中文描述

## 运行命令
- 编译：mvn compile
- 测试：mvn test
- 启动：mvn spring-boot:run
