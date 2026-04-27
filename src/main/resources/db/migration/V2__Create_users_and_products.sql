-- ============================================
-- 用户和商品表
-- ============================================

-- 1. 系统用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    email VARCHAR(200),
    avatar VARCHAR(500),
    status SMALLINT NOT NULL DEFAULT 1,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email),

    -- 索引
    INDEX idx_users_status (status)
);

-- 2. 商品表
CREATE TABLE products (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(18,2) NOT NULL DEFAULT 0,
    stock INT NOT NULL DEFAULT 0,
    status SMALLINT NOT NULL DEFAULT 1,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,

    -- 索引
    INDEX idx_products_user_id (user_id),
    INDEX idx_products_status (status),
    INDEX idx_products_create_time (create_time)
);
