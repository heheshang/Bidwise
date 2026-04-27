项目规范
技术栈
Java 21 + Spring Boot 3.x
MyBatis-Plus 持久层
MySQL 8.0
Maven 构建
包结构
com.example.项目名/ ├── controller/ # 接口层，只做参数校验和调用Service ├── service/ # 业务层，所有业务逻辑在这里 │ └── impl/ # Service实现类 ├── mapper/ # 数据访问层 ├── model/ │ ├── entity/ # 数据库实体 │ ├── dto/ # 请求参数 │ ├── vo/ # 响应结果 │ └── enums/ # 枚举 ├── config/ # 配置类 ├── common/ # 公共组件（统一返回、异常、常量） └── utils/ # 工具类

代码规范
Controller 方法不超过 15 行，只做参数校验和调用 Service
Service 每个方法只做一件事，超过 50 行必须拆分
所有对外接口使用统一返回体 Result
禁止在 Controller 中直接写 SQL 或调用 Mapper
每个 public 方法必须有 Javadoc 注释
API 设计规范
RESTful 风格：GET 查询、POST 新增、PUT 修改、DELETE 删除
统一路径前缀：/api/v1/模块名
分页查询统一用 PageRequest/PageResult
错误码统一定义在 ErrorCode 枚举中
运行命令
编译：mvn compile
测试：mvn test
启动：mvn spring-boot:run