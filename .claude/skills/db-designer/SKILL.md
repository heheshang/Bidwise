---
name: db-designer
description: 输入业务需求，按标准流程输出 需求分析→ER图→DDL→Entity 全套数据库设计
---

# 数据库设计器

## 输入
- 业务描述（如："电商系统，包含用户、商品、订单、购物车"）

## 执行流程
严格按照 .claude/rules/db-design.md 中定义的 4 步流程执行：
1. 输出需求分析表
2. 输出 ER 关系描述
3. 生成完整 DDL（写入 src/main/resources/db/migration/）
4. 生成 Entity + DTO + VO（写入对应包目录）

## 规范要求
- 遵守 database.md 中的所有命名和类型规范
- DDL 用 Flyway 格式命名
- Entity 遵守项目 CLAUDE.md 中的包结构
