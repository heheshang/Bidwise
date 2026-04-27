-- ============================================
-- OAuth2.0 认证授权中心数据库表 (PostgreSQL)
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
    CONSTRAINT uk_oauth2_clients_client_id UNIQUE (client_id)
);

CREATE INDEX idx_oauth2_clients_status ON oauth2_clients(status);

-- 2. OAuth2 访问令牌表
CREATE TABLE oauth2_access_tokens (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    user_type SMALLINT NOT NULL DEFAULT 1,
    access_token VARCHAR(500) NOT NULL,
    expires_time TIMESTAMP NOT NULL,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_oauth2_access_tokens_access_token UNIQUE (access_token)
);

CREATE INDEX idx_oauth2_access_tokens_client_id ON oauth2_access_tokens(client_id);
CREATE INDEX idx_oauth2_access_tokens_user_id ON oauth2_access_tokens(user_id);
CREATE INDEX idx_oauth2_access_tokens_expires_time ON oauth2_access_tokens(expires_time);

-- 3. OAuth2 刷新令牌表
CREATE TABLE oauth2_refresh_tokens (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    user_type SMALLINT NOT NULL DEFAULT 1,
    refresh_token VARCHAR(500) NOT NULL,
    expires_time TIMESTAMP NOT NULL,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_oauth2_refresh_tokens_refresh_token UNIQUE (refresh_token)
);

CREATE INDEX idx_oauth2_refresh_tokens_client_id ON oauth2_refresh_tokens(client_id);
CREATE INDEX idx_oauth2_refresh_tokens_user_id ON oauth2_refresh_tokens(user_id);
CREATE INDEX idx_oauth2_refresh_tokens_expires_time ON oauth2_refresh_tokens(expires_time);

-- 4. OAuth2 授权码表
CREATE TABLE oauth2_codes (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    user_type SMALLINT NOT NULL DEFAULT 1,
    code VARCHAR(200) NOT NULL,
    redirect_uri VARCHAR(500),
    scope TEXT,
    state VARCHAR(255),
    expires_time TIMESTAMP NOT NULL,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_oauth2_codes_code UNIQUE (code)
);

CREATE INDEX idx_oauth2_codes_client_id ON oauth2_codes(client_id);
CREATE INDEX idx_oauth2_codes_user_id ON oauth2_codes(user_id);
CREATE INDEX idx_oauth2_codes_expires_time ON oauth2_codes(expires_time);

-- 5. OAuth2 授权批准记录表
CREATE TABLE oauth2_approvals (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    client_id VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    user_type SMALLINT NOT NULL DEFAULT 1,
    scope VARCHAR(500) NOT NULL,
    approved SMALLINT NOT NULL DEFAULT 0,
    expires_time TIMESTAMP NOT NULL,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_oauth2_approvals_client_id ON oauth2_approvals(client_id);
CREATE INDEX idx_oauth2_approvals_user_id ON oauth2_approvals(user_id);
CREATE INDEX idx_oauth2_approvals_approved ON oauth2_approvals(approved);
