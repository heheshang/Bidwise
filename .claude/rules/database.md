# 数据库设计与访问规范

## 表命名规范

- 使用小写字母 + 下划线（snake_case）
- 表名使用复数形式
- 前缀清晰表示业务模块

**正确示例**：
```
bids
bid_items
user_bids
sys_config
```

**错误示例**：
```
Bid  # 大写
bidItem  # 驼峰
bid_item  # 单数
biditem  # 缺少下划线
```

## 必须字段

每张表**必须**包含以下四个字段：

| 字段名 | 类型 | 是否可为空 | 说明 |
|--------|------|-----------|------|
| `id` | `BIGINT` | NOT NULL | 主键，自增 |
| `create_time` | `TIMESTAMP` | NOT NULL | 创建时间，默认 `CURRENT_TIMESTAMP` |
| `update_time` | `TIMESTAMP` | NOT NULL | 更新时间，默认 `CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` |
| `is_deleted` | `SMALLINT` / `BOOLEAN` | NOT NULL | 逻辑删除，0 未删除，1 已删除，默认 0 |

**建表示例**：
```sql
CREATE TABLE bids (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    -- 其他业务字段...
    title VARCHAR(255) NOT NULL,
    price DECIMAL(18,2) NOT NULL,
    
    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0
);
```

## 实体类规范（MyBatis-Plus）

- 使用 `@TableName` 指定表名
- 使用 `@TableId` 指定主键
- 使用 `@TableField` 指定字段名（与数据库一致）
- 开启逻辑删除

**示例**：
```java
@Data
@TableName("bids")
public class Bid {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 业务字段
    private String title;
    private BigDecimal price;
    
    // 必须字段
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isDeleted;
}
```

## SQL 编写规范

### 禁止 `SELECT *`

**原因**：
- 不需要的字段增加 IO 和内存消耗
- 表结构变更后可能导致结果不匹配
- 无法利用覆盖索引优化查询

**正确示例**：
```java
// MyBatis-Plus Lambda 查询
lambdaQuery()
    .select(Bid::getId, Bid::getTitle, Bid::getPrice)
    .eq(Bid::getIsDeleted, 0)
    .list();
```

**错误示例**：
```java
// 错误：SELECT *
lambdaQuery()
    .eq(Bid::getIsDeleted, 0)
    .list();
```

### 禁止手动拼接 SQL

❌ **禁止**：
```java
// 错误：拼接 SQL，存在 SQL 注入风险
String sql = "SELECT * FROM bids WHERE id = " + id;

// 错误：拼接 LIKE 参数
String keyword = request.getKeyword();
query.last("AND title LIKE '%" + keyword + "%'");
```

✅ **正确**：
```java
// 使用参数绑定，MyBatis-Plus 自动处理
lambdaQuery()
    .like(Bid::getTitle, keyword)
    .list();
```

### LIKE 查询规范

- 前缀模糊查询 `%xxx` 无法利用索引
- 尽量使用后缀模糊查询 `xxx%`
- 全模糊查询 `%xxx%` 避免大数据量场景

## 数据类型规范

### 金额字段

- **必须使用 `DECIMAL(18,2)` / `DECIMAL(18,4)`**
- Java 类型对应 `BigDecimal`
- **禁止使用 `FLOAT` / `DOUBLE`**，会有精度丢失问题

**正确示例**：
```sql
price DECIMAL(18,2) NOT NULL
```

```java
private BigDecimal price;
```

**错误示例**：
```sql
price DOUBLE NOT NULL  -- 错误：精度丢失
```

```java
private Double price;  -- 错误：精度丢失
```

### 时间字段

- 创建时间、更新时间使用 `TIMESTAMP`
- Java 类型对应 `LocalDateTime`
- 禁止使用 `DATETIME` 过时类型
- 禁止使用 `BIGINT` 存储时间戳（可读性差）

### 布尔/逻辑删除字段

- 使用 `SMALLINT` 存储 0/1
- 禁止使用 `BIT(1)`，不同数据库兼容性差

### 字符串字段

- 明确长度的使用 `VARCHAR(n)`，n = 实际需要长度
- 不确定长度的使用 `TEXT`
- 禁止无限制使用 `VARCHAR(255)`

## MyBatis-Plus 使用规范

### CRUD 基本使用

- 简单 CRUD 使用 MyBatis-Plus 提供的方法
- 简单条件查询使用 `LambdaQueryWrapper`
- 复杂查询使用自定义 XML

**示例**：
```java
// 正确：简单查询
List<Bid> bids = this.lambdaQuery()
    .eq(Bid::getUserId, userId)
    .eq(Bid::getIsDeleted, 0)
    .orderByDesc(Bid::getCreateTime)
    .page(page)
    .list();
```

### 分页查询

- 必须使用 MyBatis-Plus 分页插件
- 禁止内存分页（查询全部再分页）

**示例**：
```java
Page<Bid> page = new Page<>(pageNum, pageSize);
Page<Bid> result = this.lambdaQuery()
    .eq(Bid::getIsDeleted, 0)
    .orderByDesc(Bid::getCreateTime)
    .page(page);
```

### 逻辑删除

- MyBatis-Plus 自动过滤已删除数据
- 查询不需要额外加 `is_deleted = 0` 条件（自动拼接）
- 需要查询已删除数据时使用 `lambdaQuery().withoutLogicDelete()`

## 事务规范

- 在 Service 层方法添加 `@Transactional`
- 读方法不需要加事务
- 指定 `rollbackFor = Exception.class`
- 事务方法不要超过 50 行，复杂业务拆分成多个方法

**正确示例**：
```java
@Override
@Transactional(rollbackFor = Exception.class)
public BidVO create(CreateBidRequest request) {
    // 业务逻辑
}
```

## 索引设计规范

- 主键字段：`id` 自增主键
- 常用查询条件必须建索引
- 联合索引遵循最左前缀原则
- 索引个数控制在 5 个以内，过多索引影响写入性能

## 禁止事项总结

| 禁止操作 | 原因 | 正确做法 |
|---------|------|---------|
| `SELECT *` | 浪费IO，无法利用覆盖索引 | 只查询需要的字段 |
| 手动拼接SQL | SQL注入风险 | 使用参数化查询 |
| 金额用 `DOUBLE`/`FLOAT` | 精度丢失 | 使用 `DECIMAL` + `BigDecimal` |
| 缺少必须字段 | 数据追溯困难 | 每张表必须有 `id`, `create_time`, `update_time`, `is_deleted` |
| 内存分页 | 大数据量时OOM | 使用分页插件 |
| 逻辑删除不使用 | 数据难以恢复，删除慢 | 使用 `is_deleted` 逻辑删除 |
| 在 Controller 写SQL | 分层混乱 | SQL 必须在 Mapper 层 |
| 跨库联表查询 | 性能差，事务难控制 | 应用层join |

## 最佳实践

1. **逻辑删除优先**：不物理删除数据，方便数据恢复和追溯
2. **参数化查询**：永远使用参数绑定，防止 SQL 注入
3. **只查需要的字段**：减少网络传输，提高查询性能
4. **合理使用索引**：常用查询条件建索引，避免过多索引
5. **批量操作**：批量插入/更新使用 MyBatis-Plus 批量方法，减少网络往返
6. **避免大事务**：事务尽量小，减少锁持有时间，提高并发

---

## 数据库设计开发流程

新增业务模块必须遵循以下四步流程：

### 步骤 1：需求分析 → 实体关系梳理

**输出**：实体列表 + 关系说明

1. 列出所有需要的实体（表）
2. 标识实体之间的关系：一对一/一对多/多对多
3. 确定每个实体的核心业务字段

**示例**：
```
需求：竞价系统
- 用户(user) ←→ 竞价(bid)：一对多，一个用户可以发布多个竞价
- 竞价(bid) ←→ 竞价项(bid_item)：一对多，一个竞价包含多个拍品
- 用户(user) ←→ 出价记录(bid_record)：一对多，一个用户可以多次出价
```

### 步骤 2：ER 图设计 → 主键/外键/唯一约束

**输出**：ER 图（文本或可视化），标注：
- **主键 PK**：每个表必须有主键 `id`
- **外键 FK**：关联关系通过外键体现（业务层面关联，不强制数据库外键约束）
- **唯一约束 UK**：需要唯一性的字段添加唯一约束

**设计要点**：
- 关联字段命名：`{表名}_id`，如 `bid_id`、`user_id`
- 唯一约束：如用户名 `username`、邮箱 `email` 需要唯一，添加 `UNIQUE` 约束
- 外键不强制使用数据库级约束，应用层保证一致性即可

**示例**：
```
bids 表：
  PK: id
  FK: user_id → users.id
  UK: (title, user_id) → 同一用户不能有相同标题的竞价

bid_items 表：
  PK: id
  FK: bid_id → bids.id
```

### 步骤 3：DDL 生成 → Flyway 命名规范 + 完整索引

#### Flyway 文件命名

文件存放路径：`src/main/resources/db/migration/`

命名格式：`V{版本}__{描述}.sql`

> 注意：两个下划线 `__` 分隔版本和描述

**示例**：
```
V1__Initial_schema.sql
V2__Add_bids_table.sql
V3__Add_bid_items_table.sql
```

#### DDL 编写要求

1. 必须包含必须字段：`id`, `create_time`, `update_time`, `is_deleted`
2. 必须标注主键、唯一约束
3. 必须为常用查询条件建立索引
4. 数据类型必须符合本规范

**完整 DDL 示例**：
```sql
-- V2__Create_bids_table.sql
CREATE TABLE bids (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    current_price DECIMAL(18,2) NOT NULL DEFAULT 0,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status SMALLINT NOT NULL DEFAULT 1,
    
    -- 必须字段
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted SMALLINT NOT NULL DEFAULT 0,
    
    -- 唯一约束
    CONSTRAINT uk_bids_title_user_id UNIQUE (title, user_id),
    
    -- 索引
    INDEX idx_bids_user_id (user_id),
    INDEX idx_bids_status (status),
    INDEX idx_bids_create_time (create_time)
);
```

#### 索引规范完整要求

- **主键索引**：自动创建，无需额外指定
- **外键字段**：必须创建索引，因为经常按此外键查询
- **常用查询条件**：where 条件中常用的字段必须创建索引
- **排序字段**：频繁排序的字段可以加入联合索引
- **唯一约束**：自动创建唯一索引，无需重复创建
- **索引数量**：单表索引不超过 5 个，过多索引影响写入性能
- **联合索引顺序**：区分度高的字段放前面，遵循最左前缀原则

### 步骤 4：代码生成 → Entity-DTO-VO 同步创建

创建表后，同步创建以下代码：

| 层级 | 文件位置 | 说明 |
|------|----------|------|
| Entity | `model/entity/Bid.java` | 对应数据库表，MyBatis-Plus 注解 |
| Mapper | `mapper/BidMapper.java` | 继承 BaseMapper\<Bid> |
| DTO | `model/dto/CreateBidRequest.java` / `UpdateBidRequest.java` | 接口入参 |
| VO | `model/vo/BidVO.java` | 接口出参 |
| Converter | `model/converter/BidConverter.java` | Entity-DTO-VO 互转 |

#### Entity 生成规范

```java
@Data
@TableName("bids")
public class Bid {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 业务字段
    private Long userId;
    private String title;
    private String description;
    private BigDecimal currentPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    
    // 必须字段
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isDeleted;
}
```

#### Mapper 生成规范

```java
public interface BidMapper extends BaseMapper<Bid> {
    // 简单 CRUD 不用写方法，BaseMapper 已提供
    // 复杂查询在这里定义方法，XML 写 SQL
}
```

#### DTO 生成规范（入参）

```java
@Data
public class CreateBidRequest {
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题长度不能超过255")
    private String title;
    
    private String description;
    
    @NotNull(message = "起拍价不能为空")
    @Min(value = 0, message = "起拍价不能小于0")
    private BigDecimal startPrice;
    
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;
    
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
}
```

#### VO 生成规范（出参）

```java
@Data
public class BidVO {
    
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal currentPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private LocalDateTime createTime;
    // 不返回 isDeleted，前端不需要
}
```

#### Converter 转换示例

```java
@Component
public class BidConverter {
    
    public Bid toEntity(CreateBidRequest request) {
        Bid bid = new Bid();
        bid.setTitle(request.getTitle());
        bid.setDescription(request.getDescription());
        bid.setCurrentPrice(request.getStartPrice());
        bid.setStartTime(request.getStartTime());
        bid.setEndTime(request.getEndTime());
        return bid;
    }
    
    public BidVO toVO(Bid entity) {
        BidVO vo = new BidVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getUserId());
        vo.setTitle(entity.getTitle());
        vo.setDescription(entity.getDescription());
        vo.setCurrentPrice(entity.getCurrentPrice());
        vo.setStartTime(entity.getStartTime());
        vo.setEndTime(entity.getEndTime());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }
}
```

### 开发流程总结

```
需求分析 → 实体关系 → ER 图设计 → DDL (Flyway) → Entity → Mapper → DTO → VO → Converter
```

每一步都不能跳过，必须同步创建所有层级代码
