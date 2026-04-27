-- 插入默认管理员用户 (密码: admin123, 已使用BCrypt加密)
INSERT INTO users (username, password, nickname, email, status)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lqkkO9S3FLnA6yKXG', '管理员', 'admin@bidwise.com', 1)
    ON CONFLICT (username) DO NOTHING;
