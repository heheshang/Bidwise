# 单元测试规范

## 测试结构

所有单元测试必须遵循 **Given-When-Then** 结构：

1. **Given**：准备测试数据，配置依赖行为
2. **When**：执行被测试方法
3. **Then**：断言结果，验证行为

**示例**：
```java
@Test
void shouldCreateBidWhenValidRequest() {
    // Given
    CreateBidRequest request = createValidRequest();
    given(bidMapper.insert(any(Bid.class))).willReturn(1);
    
    // When
    BidVO result = bidService.create(request);
    
    // Then
    assertNotNull(result);
    assertEquals(request.getTitle(), result.getTitle());
    verify(bidMapper).insert(any(Bid.class));
}
```

## 测试隔离

### 使用 `@MockBean` 隔离依赖

- Service 层测试：`@MockBean` 注入 Mapper 依赖，不访问真实数据库
- Controller 层测试：`@MockBean` 注入 Service 依赖，不访问真实业务逻辑
- 单元测试不启动完整 Spring 上下文，只测试目标类

**正确示例**：
```java
@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @InjectMocks
    private BidService bidService;

    @Mock
    private BidMapper bidMapper;

    @Test
    void shouldCreateBidWhenValidRequest() {
        // ... 测试代码
    }
}
```

**错误示例**：
```java
// 错误：启动完整上下文，测试慢，依赖真实环境
@SpringBootTest
class BidServiceTest {
    // ...
}
```

## 断言规范

- 使用 JUnit5 `org.junit.jupiter.api.Assertions.*` 进行断言
- 每个测试至少有一个断言
- 多个断言要清晰分开
- 不使用 `System.out.println` 打印结果代替断言

**正确示例**：
```java
// Then
assertNotNull(result);
assertEquals(expectedTitle, result.getTitle());
assertEquals(expectedPrice, result.getPrice());
verify(bidMapper).insert(any(Bid.class));
```

**错误示例**：
```java
// 错误：打印输出代替断言
System.out.println(result);
// 没有断言，测试无法自动判断失败
```

## 测试覆盖要求

- **每个 public 方法**必须至少有：
  1. **正向测试**：参数合法，预期成功
  2. **异常测试**：参数不合法或业务规则不满足，预期抛出异常

**示例**：
```java
// 1. 正向测试：创建成功
@Test
void shouldCreateBidWhenValidRequest() {
    // Given
    CreateBidRequest request = createValidRequest();
    given(bidMapper.insert(any(Bid.class))).willReturn(1);
    
    // When
    BidVO result = bidService.create(request);
    
    // Then
    assertNotNull(result);
    assertEquals(request.getTitle(), result.getTitle());
}

// 2. 异常测试：标题为空抛出参数异常
@Test
void shouldThrowExceptionWhenTitleIsBlank() {
    // Given
    CreateBidRequest request = createRequestWithBlankTitle();
    
    // When + Then
    BusinessException exception = assertThrows(BusinessException.class, () -> {
        bidService.create(request);
    });
    assertEquals(PARAM_ERROR.getCode(), exception.getCode());
}

// 3. 异常测试：竞价不存在抛出异常
@Test
void shouldThrowExceptionWhenBidNotExist() {
    // Given
    Long nonExistentId = 9999L;
    given(bidMapper.selectById(nonExistentId)).willReturn(null);
    
    // When + Then
    BusinessException exception = assertThrows(BusinessException.class, () -> {
        bidService.delete(nonExistentId);
    });
    assertEquals(BID_NOT_EXIST.getCode(), exception.getCode());
}
```

## 命名规范

测试类和测试方法命名要清晰表达测试意图：

- **测试类命名**：`{被测试类}Test`
- **测试方法命名**：`should{预期行为}When{测试场景}`

**示例**：
```java
class BidServiceTest {  // 正确：{被测试类}Test
    
    @Test
    void shouldCreateBidWhenValidRequest() { ... }
    
    @Test
    void shouldThrowExceptionWhenTitleIsBlank() { ... }
    
    @Test
    void shouldThrowExceptionWhenBidNotExist() { ... }
}
```

## Mock 规范

- `given()`：配置依赖方法返回值或抛出异常
- `verify()`：验证依赖方法是否被正确调用
- `any()`：匹配任意参数，`eq()`：匹配精确参数
- 不需要验证的方法不需要 `verify`

**正确示例**：
```java
// 配置返回值
given(bidMapper.selectById(id)).willReturn(bid);

// 验证调用
verify(bidMapper).selectById(eq(id));
verify(bidMapper).deleteById(eq(id));
```

## 异常测试写法

使用 `assertThrows` 断言抛出正确的异常和错误码：

```java
BusinessException exception = assertThrows(BusinessException.class, () -> {
    bidService.delete(nonExistentId);
});
assertEquals(BID_NOT_EXIST.getCode(), exception.getCode());
assertEquals(BID_NOT_EXIST.getMessage(), exception.getMessage());
```

## Controller 测试示例

```java
@WebMvcTest(BidController.class)
class BidControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidService bidService;

    @Test
    void shouldReturnBidWhenGetById() throws Exception {
        // Given
        Long id = 1L;
        BidVO bidVO = createBidVO();
        given(bidService.getDetail(id)).willReturn(bidVO);

        // When + Then
        mockMvc.perform(get("/api/v1/bids/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(bidVO.getId()));
    }

    @Test
    void shouldReturnErrorWhenBidNotExist() throws Exception {
        // Given
        Long id = 9999L;
        given(bidService.getDetail(id)).willThrow(new BusinessException(BID_NOT_EXIST));

        // When + Then
        mockMvc.perform(get("/api/v1/bids/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(BID_NOT_EXIST.getCode()));
    }
}
```

## 最佳实践

1. **一个测试方法只测一个行为**：不要在一个测试方法测多个场景
2. **测试独立**：每个测试方法不依赖其他测试方法的执行结果
3. **测试命名清晰**：从方法名就能看出测试场景和预期结果
4. **合理使用 Mock**：只 Mock 被测试类的依赖，不 Mock 被测试类本身
5. **覆盖正向和异常场景**：正常流程能跑对，错误流程能正确抛出异常
6. **不测试 get/set 方法**：Lombok 生成的方法不需要测试
7. **不测试 Spring 容器**：单元测试只测业务逻辑

## 禁止事项

| 禁止操作 | 原因 | 正确做法 |
|---------|------|---------|
| 一个测试方法多个不相关场景 | 一个失败影响其他，难以定位 | 拆分成多个测试方法 |
| 依赖数据库进行单元测试 | 测试慢，环境依赖，不稳定 | 使用 @Mock 隔离 Mapper |
| 没有断言，靠人眼判断 | 无法自动化，测试形同虚设 | 每个测试必须有断言 |
| 跳过异常场景测试 | 异常路径可能有bug没发现 | 每个public方法至少一个异常测试 |
| 测试方法命名不清晰 | 维护困难，不知道测什么 | 使用 shouldXXWhenYY 命名 |
| 使用 System.out 代替断言 | 无法自动化判断结果 | 必须使用 Assertions |

---

## OAuth2 专项测试规范

### 测试命名规范

- **测试类**：`{被测类名}Test` → 例如 `OAuth2ClientServiceImplTest`
- **测试方法**：`test_方法名_场景` → 例如 `test_validClientFromCache_clientNotExists`

### 必须覆盖的测试场景

每个 Service 方法至少覆盖：
1. **正常流程**：参数合法，业务成功
2. **参数校验失败**：如 clientId 不存在
3. **状态异常**：如客户端被禁用

### OAuth2 专项测试要求

| 测试场景 | 要求 |
|---------|------|
| 四种授权模式 | 每种模式至少一个端到端测试 |
| Token 过期场景 | 测试过期 Token 被正确拒绝 |
| Token 刷新场景 | 测试刷新令牌获取新访问令牌 |
| 并发颁发 Token | 测试并发场景下 Token 不冲突 |
| redirect_uri 校验 | 测试正确匹配 / 不匹配 / 缺失三种情况 |
| scope 越权测试 | 测试客户端请求未授权 scope 被拒绝 |

### 测试结构示例

遵循 Given-When-Then 结构：

```java
@Test
void test_createAuthorizationCode_validRequest() {
    // Given：准备 OAuth2Client + 用户数据 + 合法参数
    OAuth2ClientDO client = createValidClient();
    given(oAuth2ClientMapper.selectByClientId(eq(CLIENT_ID))).willReturn(client);
    
    // When：调用授权接口
    String code = oAuth2CodeService.createAuthorizationCode(request, userId);
    
    // Then：验证授权码生成结果 / 存储结果
    assertNotNull(code);
    verify(oAuth2CodeMapper).insert(any(OAuth2CodeDO.class));
}

@Test
void test_createAuthorizationCode_clientDisabled() {
    // Given：准备被禁用的客户端
    OAuth2ClientDO client = createDisabledClient();
    given(oAuth2ClientMapper.selectByClientId(eq(CLIENT_ID))).willReturn(client);
    
    // When + Then：验证抛出异常，错误码正确
    BusinessException exception = assertThrows(BusinessException.class, () -> {
        oAuth2CodeService.createAuthorizationCode(request, userId);
    });
    assertEquals(OAUTH2_CLIENT_DISABLED.getCode(), exception.getCode());
}
```

---

## RBAC 权限控制专项测试规范

### 测试命名规范

- **测试类**：`{被测类名}Test` → 例如 `RoleServiceImplTest`
- **测试方法**：`test_方法名_场景` → 例如 `test_createRole_duplicateCode`

### 必须覆盖的测试场景

每个 Service 方法至少覆盖：
1. **正常流程**：参数合法，业务成功
2. **参数校验失败**：如角色名重复、菜单不存在
3. **状态异常**：如角色被禁用、菜单被禁用

### RBAC 专项测试要求

| 测试场景 | 要求 |
|---------|------|
| 角色 CRUD | 完整流程测试 |
| 菜单树形结构 | 三级结构构建测试（目录→菜单→按钮） |
| 权限分配与回收 | 分配后正确授权，回收后正确禁用 |
| 超级管理员 | 权限放行逻辑测试 |
| 数据权限 | 五种范围各一个测试 |
| 角色删除 | 级联清理权限关系测试 |
| 菜单删除 | 级联清理权限关系测试 |
| 缓存一致性 | 修改后缓存是否正确清除 |
| 并发分配权限 | 并发场景下权限数据一致性 |

### 测试结构示例

遵循 Given-When-Then 结构：

```java
@Test
void test_createRole_normal() {
    // Given：准备角色数据
    CreateRoleReqVO request = createValidRole();
    given(roleMapper.selectByCode(eq(ROLE_CODE))).willReturn(null);

    // When：调用创建接口
    RoleVO result = roleService.createRole(request);

    // Then：验证结果
    assertNotNull(result);
    assertEquals(request.getCode(), result.getCode());
    verify(roleMapper).insert(any(RoleDO.class));
}

@Test
void test_createRole_duplicateCode() {
    // Given：角色编码已存在
    CreateRoleReqVO request = createValidRole();
    given(roleMapper.selectByCode(eq(ROLE_CODE))).willReturn(existingRole);

    // When + Then：验证抛出异常
    BusinessException exception = assertThrows(BusinessException.class, () -> {
        roleService.createRole(request);
    });
    assertEquals(ROLE_CODE_DUPLICATE.getCode(), exception.getCode());
}
```
