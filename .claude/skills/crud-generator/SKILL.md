# CRUD Generator Skill

自动生成标准 CRUD 全栈代码，遵循项目分层规范。

## 触发词

`生成CRUD`, `crud生成`, `创建CRUD`, `新增模块`, `生成代码` 触发此技能。

## 功能

根据输入的表名和字段信息，自动生成以下完整代码：

```
model/
├── entity/
│   └── {Entity}.java           # 实体类
├── dto/
│   ├── Create{Entity}Request.java   # 创建请求
│   └── Update{Entity}Request.java   # 更新请求
├── vo/
│   └── {Entity}VO.java         # 视图对象
└── converter/
    └── {Entity}Converter.java  # 对象转换器

mapper/
└── {Entity}Mapper.java         # Mapper接口

service/
├── {Entity}Service.java        # 服务接口
└── impl/
    └── {Entity}ServiceImpl.java # 服务实现

controller/
└── {Entity}Controller.java     # Controller

test/
└── {Entity}ServiceTest.java    # 单元测试 (正向+异常)
```

## 生成规则

严格遵循项目规范：

### 1. Entity
- 必须包含四个必须字段：`id`, `createTime`, `updateTime`, `isDeleted`
- 使用 `@TableName` 指定表名
- 使用 `@TableId(type = IdType.AUTO)`
- Lombok `@Data` 注解

### 2. DTO
- 创建请求 `CreateXxxRequest`：包含所有需要用户输入的字段
- 更新请求 `UpdateXxxRequest`：包含 `id` + 所有可更新字段
- 所有非空字段添加 `@NotNull`/`@NotBlank` 校验注解
- 添加 `message` 错误信息

### 3. VO
- 包含需要返回给前端的所有字段
- 不包含 `isDeleted`
- 使用 Lombok `@Data`

### 4. Converter
- `toEntity(CreateXxxRequest)` → 创建实体
- `toVO(Entity)` → 转换为 VO

### 5. Mapper
- 继承 `BaseMapper<Entity>`
- 无多余方法

### 6. Service
- 接口定义：`pageQuery`, `getDetail`, `create`, `update`, `delete`
- 实现类：
    - `pageQuery`：分页查询，按 `createTime` 降序
    - `getDetail`：按ID查询，不存在抛异常
    - `create`：DTO 转 Entity 保存
    - `update`：检查存在，更新
    - `delete`：检查存在，删除
- `@Transactional(rollbackFor = Exception.class)` 标注写方法

### 7. Controller
- 标准 REST 接口：
    - `GET /api/v1/{table}` → 分页查询
    - `GET /api/v1/{table}/{id}` → 获取详情
    - `POST /api/v1/{table}` → 创建
    - `PUT /api/v1/{table}/{id}` → 更新
    - `DELETE /api/v1/{table}/{id}` → 删除
- 所有返回类型都是 `Result<xxx>`
- 参数校验使用 `@Valid`

### 8. 单元测试
- 每个 public 方法：一个正向测试 + 一个异常测试
- 遵循 Given-When-Then 结构
- 使用 `@Mock` 注入 Mapper 依赖

## 使用方式

当用户说 "为表 xxx 生成 CRUD" 时：

1. 确认表名、实体名、字段列表
2. 按上述规范生成所有文件
3. 每个文件写入正确路径
4. 输出生成文件列表

## 示例输入

```
为表 bids 生成 CRUD，字段：
- userId (Long, 必填)
- title (String, 必填, 最大255)
- description (String, 可选)
- currentPrice (BigDecimal, 必填, >=0)
- startTime (LocalDateTime, 必填)
- endTime (LocalDateTime, 必填)
- status (Integer, 默认1)
```

## 示例输出

列出生成的文件：

```
✅ CRUD 代码生成完成：
- `model/entity/Bid.java`
- `model/dto/CreateBidRequest.java`
- `model/dto/UpdateBidRequest.java`
- `model/vo/BidVO.java`
- `model/converter/BidConverter.java`
- `mapper/BidMapper.java`
- `service/BidService.java`
- `service/impl/BidServiceImpl.java`
- `controller/BidController.java`
- `test/BidServiceImplTest.java`
```

## 代码规范检查

生成后自动检查：
- [x] 表命名符合规范（小写下划线复数）
- [x] 包含四个必须字段
- [x] 金额使用 BigDecimal
- [x] Controller 返回都是 Result<T>
- [x] Service 抛出异常，不返回 Result
- [x] 参数校验完整
- [x] 每个 public 方法有对应的测试
