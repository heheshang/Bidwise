# RBAC 分层架构规范

## 五层架构

### Controller 层
- @Tag 注解标注模块名（如 @Tag(name = "管理后台 - 权限管理")）
- 每个方法加 @Operation(summary = "xxx")
- 参数校验用 @Valid + JSR 380
- 返回值统一 ApiResult<T>，使用 static import success()
- 权限校验用 @PreAuthorize("@perm.check('system:role:create')")
- 权限标识格式：模块:资源:操作（如 system:role:create）

### Service 层
- 接口 + 实现分离
- 接口方法必须有完整 Javadoc（参数、返回值、功能说明）
- Impl 中用 @Resource 注入依赖（不用 @Autowired）
- 校验方法命名：validateXxx，如 validateRoleExists
- 缓存相关方法命名：xxxFromCache，如 getRoleListFromCache

### DAL 层 - PostgreSQL Mapper
- 继承 ExtBaseMapper<T>（不是原生 BaseMapper）
- 查询方法用 default 方法 + LambdaQueryChain
- 分页查询方法命名：queryPage
- 单条查询方法命名：findByXxx
- 批量查询方法命名：findListByXxx

### DAL 层 - Redis Cache
- @Repository 注解
- 注入 StringRedisTemplate
- 方法命名：get / set / delete / deleteAll
- 缓存 key 定义在 CacheKeyConstants 中
- 权限缓存使用 Hash 结构，提升查询效率

### Converter 层
- 使用 MapStruct，@Mapper 注解
- 定义 INSTANCE 单例：PermissionConverter INSTANCE = Mappers.getMapper(PermissionConverter.class)
- DO ↔ VO 的转换方法命名：toXxx
- 禁止使用 BeanUtils.copyProperties

## 类之间的调用关系
Controller → Service（只调接口，不调 Impl）
ServiceImpl → Mapper + RedisCache + 其他 Service
Converter：在 Controller 或 ServiceImpl 中调用 INSTANCE.toXxx()
