-- ============================================
-- OAuth2.0 认证授权中心数据库表
-- ============================================

-- 1. OAuth2 客户端信息表
CREATE TABLE oauth2_clients (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id VARCHAR(100) NOT NULL,
    client_secret VARCHAR(255) NOT NULL,
    client_name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    redirect_uris TEXT NOT NULL,
    authorized_grant_types VARCHAR(500) NOT NULL,
    scope TEXT,
    auto_approve SMALLINT NOT NULL DEFAULT 0,
    access_token_validity_seconds INT NOT NULL DEFAULT 1800,
    refresh_token_validity_seconds INT NOT NULL DEFAULT 86400,
    status SMALLINT NOT NULL DEFAULT 1,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_oauth2_clients_client_id UNIQUE (client_id),

    -- 索引
    INDEX idx_oauth2_clients_status (status)
);

-- 2. OAuth2 访问令牌表
CREATE TABLE oauth2_access_tokens (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    access_token VARCHAR(500) NOT NULL,
    expiration_time TIMESTAMP NOT NULL,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_oauth2_access_tokens_access_token UNIQUE (access_token),

    -- 索引
    INDEX idx_oauth2_access_tokens_client_id (client_id),
    INDEX idx_oauth2_access_tokens_user_id (user_id),
    INDEX idx_oauth2_access_tokens_expiration_time (expiration_time)
);

-- 3. OAuth2 刷新令牌表
CREATE TABLE oauth2_refresh_tokens (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    refresh_token VARCHAR(500) NOT NULL,
    access_token_id BIGINT NOT NULL,
    expiration_time TIMESTAMP NOT NULL,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_oauth2_refresh_tokens_refresh_token UNIQUE (refresh_token),

    -- 索引
    INDEX idx_oauth2_refresh_tokens_client_id (client_id),
    INDEX idx_oauth2_refresh_tokens_user_id (user_id),
    INDEX idx_oauth2_refresh_tokens_access_token_id (access_token_id)
);

-- 4. OAuth2 授权码表
CREATE TABLE oauth2_codes (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    code VARCHAR(200) NOT NULL,
    redirect_uri VARCHAR(500),
    scope TEXT,
    expiration_time TIMESTAMP NOT NULL,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_oauth2_codes_code UNIQUE (code),

    -- 索引
    INDEX idx_oauth2_codes_client_id (client_id),
    INDEX idx_oauth2_codes_user_id (user_id),
    INDEX idx_oauth2_codes_expiration_time (expiration_time)
);

-- 5. OAuth2 授权批准记录表
CREATE TABLE oauth2_approvals (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    scope VARCHAR(500) NOT NULL,
    status SMALLINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 索引
    INDEX idx_oauth2_approvals_client_id (client_id),
    INDEX idx_oauth2_approvals_user_id (user_id),
    INDEX idx_oauth2_approvals_status (status)
);
