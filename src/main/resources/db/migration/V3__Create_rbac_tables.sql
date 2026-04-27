-- RBAC 权限管理 - 角色表 (PostgreSQL)
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(64) NOT NULL,
    code VARCHAR(64) NOT NULL,
    sort INT NOT NULL DEFAULT 0,
    status SMALLINT NOT NULL DEFAULT 0,
    type SMALLINT NOT NULL,
    data_scope SMALLINT NOT NULL DEFAULT 1,
    data_scope_dept_ids VARCHAR(2048),
    remark VARCHAR(512),
    tenant_id BIGINT NOT NULL DEFAULT 0,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_sys_role_code_tenant_id UNIQUE (code, tenant_id)
);

CREATE INDEX idx_sys_role_tenant_id ON sys_role(tenant_id);
CREATE INDEX idx_sys_role_status ON sys_role(status);

-- RBAC 权限管理 - 菜单表（全局共享）
CREATE TABLE sys_menu (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(64) NOT NULL,
    permission VARCHAR(128),
    type SMALLINT NOT NULL,
    sort INT NOT NULL DEFAULT 0,
    parent_id BIGINT NOT NULL DEFAULT 0,
    path VARCHAR(256),
    icon VARCHAR(128),
    component VARCHAR(256),
    component_name VARCHAR(64),
    status SMALLINT NOT NULL DEFAULT 0,
    visible BOOLEAN NOT NULL DEFAULT TRUE,
    keep_alive BOOLEAN NOT NULL DEFAULT FALSE,
    always_show BOOLEAN NOT NULL DEFAULT FALSE,

    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_sys_menu_permission UNIQUE (permission)
);

CREATE INDEX idx_sys_menu_parent_id ON sys_menu(parent_id);
CREATE INDEX idx_sys_menu_status ON sys_menu(status);

-- RBAC 权限管理 - 角色菜单关联表
CREATE TABLE sys_role_menu (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    tenant_id BIGINT NOT NULL DEFAULT 0,

    -- 必须字段
    creator BIGINT,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater BIGINT,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_sys_role_menu_role_id_menu_id_tenant_id UNIQUE (role_id, menu_id, tenant_id)
);

CREATE INDEX idx_sys_role_menu_role_id ON sys_role_menu(role_id);
CREATE INDEX idx_sys_role_menu_menu_id ON sys_role_menu(menu_id);
CREATE INDEX idx_sys_role_menu_tenant_id ON sys_role_menu(tenant_id);

-- RBAC 权限管理 - 用户角色关联表
CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    -- 必须字段
    creator BIGINT,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updater BIGINT,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted SMALLINT NOT NULL DEFAULT 0,

    -- 唯一约束
    CONSTRAINT uk_sys_user_role_user_id_role_id UNIQUE (user_id, role_id)
);

CREATE INDEX idx_sys_user_role_user_id ON sys_user_role(user_id);
CREATE INDEX idx_sys_user_role_role_id ON sys_user_role(role_id);
