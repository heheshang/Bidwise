# 数据库设计流程

## 设计步骤（必须按顺序执行）

### 第1步：需求分析
- 列出所有业务实体（用户、订单、商品……）
- 明确实体之间的关系（一对一、一对多、多对多）
- 标注每个实体的核心属性和业务约束

### 第2步：ER 图设计
- 用文字描述 ER 关系图，格式：
  用户(User) 1---N 订单(Order) N---N 商品(Product)
- 标注主键、外键、唯一约束
- 多对多关系必须拆中间表（如 order_product）

### 第3步：DDL 生成
- 每张表必须包含基础字段：
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted  TINYINT NOT NULL DEFAULT 0
- 字段类型规范：
  - 金额用 DECIMAL(10,2)，禁止用 FLOAT/DOUBLE
  - 状态字段用 TINYINT + 注释说明每个值的含义
  - 字符串默认 VARCHAR(255)，超长文本用 TEXT
- 索引规范：
  - 外键字段必须建索引
  - 高频查询条件建联合索引（遵循最左前缀原则）
  - 唯一业务字段建唯一索引（如手机号、邮箱）
- DDL 文件放在 src/main/resources/db/migration/ 下，用 Flyway 格式命名：
  V1__create_user_table.sql
  V2__create_order_table.sql

### 第4步：Entity 生成
- 根据 DDL 生成 Entity 类，严格遵守 database.md 中的 Entity 规范
- 同步生成 DTO 和 VO（不暴露 is_deleted 等内部字段）

## 禁止事项
- ❌ 禁止跳过需求分析直接建表
- ❌ 禁止金额字段用浮点类型
- ❌ 禁止没有注释的字段和表
- ❌ 禁止多对多关系不拆中间表
