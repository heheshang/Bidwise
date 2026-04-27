# 异常处理规范

## 异常分类

### 1. BusinessException - 业务异常
- **用途**：业务逻辑错误，如参数不合法、业务规则不满足、资源不存在等
- **继承**：继承 `RuntimeException`
- **包含**：错误码 `ErrorCode` + 错误消息

### 2. SystemException - 系统异常
- **用途**：系统级错误，如数据库连接失败、第三方服务调用失败、IO 错误等
- **继承**：继承 `RuntimeException`
- **包含**：错误码 `ErrorCode` + 错误消息 + 原始异常

### 3. ErrorCode - 错误码枚举
- 所有错误码统一在 `ErrorCode` 枚举中定义
- 错误码分段：
  - `1xxx` - 参数校验错误
  - `2xxx` - 业务错误
  - `3xxx` - 权限相关错误
  - `4xxx` - 资源不存在
  - `5xxx` - 系统错误

**示例**：
```java
public enum ErrorCode {
    
    // 1xxx - 参数校验错误
    PARAM_ERROR(1001, "参数错误"),
    PARAM_BLANK(1002, "参数不能为空"),
    
    // 2xxx - 业务错误
    BID_NOT_EXIST(2001, "竞价不存在"),
    BID_ALREADY_CLOSED(2002, "竞价已关闭"),
    
    // 3xxx - 权限错误
    UNAUTHORIZED(3001, "未授权"),
    FORBIDDEN(3002, "禁止访问"),
    
    // 4xxx - 资源不存在
    RESOURCE_NOT_FOUND(4001, "资源不存在"),
    
    // 5xxx - 系统错误
    SYSTEM_ERROR(5001, "系统内部错误"),
    DB_ERROR(5002, "数据库操作错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
```

## 异常定义示例

```java
/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
```

```java
/**
 * 系统异常
 */
public class SystemException extends RuntimeException {

    private final int code;

    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
```

## 全局异常处理

使用 `@RestControllerAdvice` 统一拦截所有异常，转换为统一响应格式。

### 核心职责
1. 捕获所有异常，包括：
   - `BusinessException` - 业务异常
   - `SystemException` - 系统异常
   - `MethodArgumentNotValidException` - 参数校验失败
   - `BindException` - 参数绑定失败
   - `ConstraintViolationException` - 参数校验失败
   - `NotFoundException` - 资源不存在
   - `Exception` - 其他未知异常

2. 记录日志：系统异常打印 ERROR 级别日志，业务异常打印 INFO 级别日志

3. 转换为统一响应格式：
```json
{
  "code": "错误码",
  "message": "错误信息",
  "data": null
}
```
@
### 代码示例

```java
/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.info("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    public Result<Void> handleSystemException(SystemException e) {
        log.error("系统异常: code={}, message={}", e.getCode(), e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.info("参数校验失败: {}", message);
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("未知异常: ", e);
        return Result.error(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMessage());
    }
}
```

## 抛出异常规则

### 正确做法

```java
// 业务错误，抛出 BusinessException
if (bid == null) {
    throw new BusinessException(ErrorCode.BID_NOT_EXIST);
}

// 需要自定义错误消息
throw new BusinessException(ErrorCode.PARAM_ERROR, "竞价ID不能为空");

// 系统错误，包装原始异常抛出 SystemException
try {
    thirdPartyService.call();
} catch (IOException e) {
    throw new SystemException(ErrorCode.SYSTEM_ERROR, e);
}
```

### 禁止做法

❌ **禁止 Controller 层使用 try-catch**：
```java
// 错误：Controller 不需要 try-catch，交给全局异常处理器处理
@PostMapping
public Result<BidVO> create(@Valid @RequestBody CreateBidRequest request) {
    try {
        BidVO bid = bidService.create(request);
        return Result.success(bid);
    } catch (Exception e) {
        log.error("创建失败", e);
        return Result.error(500, "创建失败");
    }
}
```

❌ **禁止捕获异常后吞掉不处理**：
```java
// 错误：吞掉异常，问题无法定位
try {
    bidService.delete(id);
} catch (Exception e) {
    // 什么都不做
}
```

❌ **禁止返回错误码却不抛出异常**：
```java
// 错误：应该抛异常，不能返回 Result.error 给 Service
public Result<Void> delete(Long id) {
    Bid bid = this.getById(id);
    if (bid == null) {
        return Result.error(ErrorCode.BID_NOT_EXIST);
    }
    this.removeById(id);
    return Result.success();
}
```

## 异常处理原则

### 1. 早抛出
- 发现错误立即抛出异常，不要往下传递
- Service 层发现错误直接抛，不需要返回 Result

### 2. 晚捕获
- 只有全局异常处理器捕获，业务代码不捕获
- Controller 层不处理异常，交给全局异常处理器统一处理

### 3. 分类清晰
- 业务异常用 `BusinessException`
- 系统异常用 `SystemException`
- 不直接抛出 `Exception` 或 `RuntimeException`

### 4. 日志规范
- 业务异常：INFO 级别，不需要栈跟踪（预期内的错误）
- 系统异常：ERROR 级别，需要打印栈跟踪（意外错误）
- 未知异常：ERROR 级别，必须打印栈跟踪

## 依赖调用异常处理

### Service 层调用 Mapper
- Mapper 出错会抛出异常，不需要 try-catch，直接往上抛
- 全局异常处理器会捕获处理

### Service 层调用第三方服务
- 捕获第三方异常，包装成 `SystemException` 抛出
```java
try {
    return aiClient.chat(request);
} catch (AiApiException e) {
    throw new SystemException(ErrorCode.AI_SERVICE_ERROR, e);
}
```

## 禁止事项总结

| 禁止操作 | 原因 | 正确做法 |
|---------|------|---------|
| Controller 层 try-catch | 重复代码，违反 DRY | 交给全局异常处理器处理 |
| Service 层返回 Result<T> | 异常应该抛出，不能返回 | Service 方法返回数据，错误抛异常 |
| 吞掉异常不处理 | 问题无法定位，调试困难 | 要么抛出去，要么记录日志 |
| 直接抛出 Exception | 异常分类不清晰，不好处理 | 使用 BusinessException 或 SystemException |
| 在异常消息中暴露敏感信息 | 安全风险 | 只返回友好提示，敏感信息打日志 |
| 打印完整栈跟踪给客户端 | 安全风险，信息泄露 | 只返回错误码和消息，栈跟踪打日志 |
