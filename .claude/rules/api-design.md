# API 设计规范

## REST API 命名规范

### 路径命名
- 使用小写字母 + 连字符（kebab-case），不使用驼峰和下划线
- 使用名词复数表示资源集合
- 遵循 RESTful 风格

**正确示例**：
```
GET /api/v1/bids
GET /api/v1/bids/{id}
POST /api/v1/bids
PUT /api/v1/bids/{id}
DELETE /api/v1/bids/{id}
```

**错误示例**：
```
GET /api/v1/getBid
POST /api/v1/add_bid
PUT /api/v1/updateBid
```

### HTTP 方法规范

| 方法 | 用途 | 幂等 |
|------|------|------|
| GET | 获取资源/列表 | 是 |
| POST | 创建资源 | 否 |
| PUT | 完整更新资源 | 是 |
| PATCH | 部分更新资源 | 否 |
| DELETE | 删除资源 | 是 |

### 版本控制
- 在路径中包含 API 版本：`/api/v1/...`
- 不支持的版本及时下线

## 请求/响应规范

### 请求参数
- **路径参数**：用于获取单个资源、删除资源 `/{id}`
- **查询参数**：用于分页、排序、过滤条件 `?page=1&size=10`
- **请求体**：用于 POST/PUT 创建和更新资源，使用 JSON 格式

### 分页参数约定
```
page - 页码，从 1 开始
size - 每页大小，默认 10
sort - 排序字段，如：createTime,desc
```

### 统一响应格式

所有 API 必须返回统一格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- `code`：状态码，200 表示成功，非 200 表示失败
- `message`：提示信息，失败时填写错误描述
- `data`：返回数据，成功时返回具体数据，失败可为 null

分页响应格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [],
    "total": 100,
    "page": 1,
    "size": 10
  }
}
```

## 状态码规范

### 业务状态码（code 字段）
- `200` - 成功
- `400` - 请求参数错误
- `401` - 未授权
- `403` - 禁止访问
- `404` - 资源不存在
- `500` - 服务器内部错误
- 自定义业务错误使用 1xxx 段

### HTTP 状态码
- 始终返回 `200 OK`，业务错误通过 `code` 字段区分
- 异常由全局异常处理器处理并转换为统一响应

## 参数校验规范

- 使用 Jakarta Bean Validation 注解进行参数校验
- Controller 层必须对入参进行校验
- 常用注解：
  - `@NotNull` - 非 null 校验
  - `@NotBlank` - 字符串非空校验
  - `@NotEmpty` - 集合非空校验
  - `@Min`/`@Max` - 数值范围校验
  - `@Pattern` - 正则匹配

**示例**：
```java
public  class CreateBidRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    
    @NotNull(message = "价格不能为空")  
    @Min(value = 0, message = "价格不能小于0")
    private BigDecimal price;
}
```

## 错误处理规范

- 自定义业务异常继承 `RuntimeException`
- 全局异常处理器统一捕获并转换为统一响应
- 错误信息要清晰明确，方便定位问题
- 敏感信息（如栈跟踪）不返回给客户端

## 安全性规范

- 不返回敏感信息（密码、token、密钥等）给客户端
- 接口必须做权限校验
- 用户只能访问自己的数据
- 对输入参数进行防注入处理

## 日志规范

- Controller 层入参和出参必须打印日志（通过 AOP）
- 日志格式：`[API] method={} path={} params={} cost={}ms`
- 敏感信息不打日志

## 代码示例

```java
/**
 * 竞价管理接口
 */
@RestController
@RequestMapping("/api/v1/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    /**
     * 分页查询竞价列表
     */
    @GetMapping
    public Result<PageVO<BidVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            BidQuery query) {
        PageVO<BidVO> result = bidService.pageQuery(page, size, query);
        return Result.success(result);
    }

    /**
     * 获取竞价详情
     */
    @GetMapping("/{id}")
    public Result<BidVO> getById(@PathVariable Long id) {
        BidVO bid = bidService.getDetail(id);
        return Result.success(bid);
    }

    /**
     * 创建竞价
     */
    @PostMapping
    public Result<BidVO> create(@Valid @RequestBody CreateBidRequest request) {
        BidVO bid = bidService.create(request);
        return Result.success(bid);
    }

    /**
     * 更新竞价
     */
    @PutMapping("/{id}")
    public Result<BidVO> update(@PathVariable Long id, @Valid @RequestBody UpdateBidRequest request) {
        BidVO bid = bidService.update(id, request);
        return Result.success(bid);
    }

    /**
     * 删除竞价
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bidService.delete(id);
        return Result.success();
    }
}
```

## 返回值规范

- **禁止直接返回实体对象或集合**，必须包装在 `Result<T>` 中
- **禁止返回 null**，即使没有数据也要返回 `Result.success()` 或 `Result.success(null)`
- 即使操作失败，也返回 HTTP 200，通过 `code` 字段表示错误

**正确示例**：
```java
// 正确
public Result<BidVO> getById(@PathVariable Long id) {
    BidVO bid = bidService.getDetail(id);
    return Result.success(bid);
}

// 正确（无数据返回）
public Result<Void> delete(@PathVariable Long id) {
    bidService.delete(id);
    return Result.success();
}
```

**错误示例**：
```java
// 错误：直接返回实体
public BidVO getById(@PathVariable Long id) {
    return bidService.getDetail(id);
}

// 错误：直接返回集合
public List<BidVO> list() {
    return bidService.list();
}
```

## 最佳实践

1. **单一职责**：一个 Controller 对应一个资源，不混合不同资源的操作
2. **短小精悍**：Controller 方法不超过15行，复杂逻辑交给 Service
3. **参数校验**：所有入参必须校验，不信任客户端输入
4. **禁止返回实体**：禁止直接返回 JPA 实体给前端，必须通过 VO 转换
5. **必须包装 Result**：所有接口返回值必须是 `Result<T>`，不允许其他类型
6. **清晰命名**：方法名清晰表达用途，如 `getById`、`create`、`update`、`delete`
7. **文档注释**：每个 Controller 和方法添加 JavaDoc 注释说明用途
