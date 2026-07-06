# FUOJ Backend Microservice

FUOJ Backend Microservice 是一个在线判题系统的后端微服务项目，基于 Spring Boot、Spring Cloud Alibaba、Spring Cloud Gateway、Nacos、OpenFeign、RabbitMQ、Redis、MySQL 和 MyBatis-Plus 构建。

项目将核心能力拆分为用户服务、题目服务、判题服务和网关服务，并通过公共模块、模型模块和服务客户端模块复用通用能力。题目提交后会进入 RabbitMQ 消息队列，由判题服务异步消费并调用代码沙箱完成判题。

## 技术栈

| 分类 | 技术 |
| --- | --- |
| 基础框架 | Spring Boot 2.x、Spring Cloud 2021.0.5、Spring Cloud Alibaba 2021.0.5.0 |
| 服务治理 | Nacos Discovery、OpenFeign |
| 网关 | Spring Cloud Gateway |
| 限流治理 | Sentinel |
| 数据存储 | MySQL、Redis |
| ORM | MyBatis、MyBatis-Plus |
| 消息队列 | RabbitMQ |
| API 文档 | Knife4j |
| 工具库 | Lombok、Hutool、Gson、EasyExcel、Apache Commons |

## 模块说明

| 模块 | 说明 | 默认端口 |
| --- | --- | --- |
| `fuoj-backend-gateway` | 统一网关，负责按路径转发用户、题目和判题服务请求 | `8101` |
| `fuoj-backend-user-service` | 用户注册、登录、注销、用户管理、当前登录用户信息 | `8102` |
| `fuoj-backend-question-service` | 题目增删改查、题目提交、提交记录查询、发送判题消息 | `8103` |
| `fuoj-backend-judge-service` | 消费判题消息、调用代码沙箱、执行判题策略、回写提交结果 | `8104` |
| `fuoj-backend-service-client` | OpenFeign 客户端定义，封装服务间调用接口 | - |
| `fuoj-backend-model` | 实体、DTO、VO、枚举和代码沙箱模型 | - |
| `fuoj-backend-common` | 通用响应、异常、常量、注解、工具类和 JSON 配置 | - |

## 服务链路

```text
客户端
  |
  v
Gateway :8101
  |-- /api/user/**     -> fuoj-backend-user-service
  |-- /api/question/** -> fuoj-backend-question-service
  |-- /api/judge/**    -> fuoj-backend-judge-service

题目提交流程：
用户提交代码
  -> question-service 保存 question_submit，状态为 WAITING
  -> question-service 发送消息到 RabbitMQ
  -> judge-service 消费 code_queue
  -> judge-service 调用代码沙箱执行代码
  -> judge-service 根据执行结果判题并更新 question_submit
```

RabbitMQ 默认配置：

| 配置项 | 值 |
| --- | --- |
| Exchange | `code_exchange` |
| Exchange 类型 | `direct` |
| Queue | `code_queue` |
| Routing Key | `my_routingKey` |

## 环境要求

本地启动前请准备以下服务：

| 服务 | 默认配置 |
| --- | --- |
| JDK | Java 8 |
| Maven | 3.6+ |
| MySQL | `localhost:3306`，数据库名 `fuoj` |
| Redis | `localhost:6379`，使用 `database: 1` |
| Nacos | `127.0.0.1:8848` |
| RabbitMQ | `localhost:5672`，用户名/密码 `guest/guest` |
| 代码沙箱 | `http://localhost:8090/executeCode`，请求头 `auth: secretKey` |

当前配置文件中的数据库账号为：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fuoj
    username: root
    password: 123456
```

如本地配置不同，请修改各服务的 `src/main/resources/application.yml`。

## 快速启动

1. 启动本地依赖服务

   确保 MySQL、Redis、Nacos、RabbitMQ 已经启动，并创建好 `fuoj` 数据库。

2. 安装项目依赖

   ```bash
   mvn clean install -DskipTests
   ```

3. 启动微服务

   推荐在 IDE 中分别运行以下启动类：

   | 服务 | 启动类 |
   | --- | --- |
   | 用户服务 | `com.fantasy.fuojbackenduserservice.FuojBackendUserServiceApplication` |
   | 题目服务 | `com.fantasy.fuojbackendquestionservice.FuojBackendQuestionServiceApplication` |
   | 判题服务 | `com.fantasy.fuojbackendjudgeservice.FuojBackendJudgeServiceApplication` |
   | 网关服务 | `com.fantasy.fuojbackendgateway.FuojBackendGatewayApplication` |

   建议启动顺序：

   ```text
   Nacos / MySQL / Redis / RabbitMQ / 代码沙箱
     -> user-service
     -> question-service
     -> judge-service
     -> gateway
   ```

   判题服务启动时会自动初始化 RabbitMQ 的 `code_exchange` 和 `code_queue`。

4. 通过网关访问接口

   ```text
   http://localhost:8101
   ```

## 接口入口

网关路由：

| 路径 | 目标服务 |
| --- | --- |
| `/api/user/**` | 用户服务 |
| `/api/question/**` | 题目服务 |
| `/api/judge/**` | 判题服务 |

常用接口：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `POST` | `/api/user/register` | 用户注册 |
| `POST` | `/api/user/login` | 用户登录 |
| `POST` | `/api/user/logout` | 用户注销 |
| `GET` | `/api/user/get/login` | 获取当前登录用户 |
| `POST` | `/api/question/add` | 创建题目 |
| `POST` | `/api/question/delete` | 删除题目 |
| `POST` | `/api/question/update` | 管理员更新题目 |
| `GET` | `/api/question/get/vo` | 获取题目脱敏信息 |
| `POST` | `/api/question/list/page/vo` | 分页查询题目 |
| `POST` | `/api/question/my/list/page/vo` | 查询当前用户创建的题目 |
| `POST` | `/api/question/edit` | 编辑题目 |
| `POST` | `/api/question/question_submit/do` | 提交代码判题 |
| `POST` | `/api/question/question_submit/list/page` | 分页查询提交记录 |

内部服务调用接口位于各服务的 `/inner/**` 路径下，由 OpenFeign 客户端使用，不建议直接暴露给前端。

## 代码沙箱配置

判题服务通过 `codesandbox.type` 选择代码沙箱实现：

```yaml
codesandbox:
  type: remote
```

可选实现：

| 值 | 实现类 | 说明 |
| --- | --- | --- |
| `example` | `ExampleCodeSandbox` | 示例沙箱，直接返回模拟结果，适合联调主流程 |
| `remote` | `RemoteCodeSandbox` | 调用本地/远程沙箱接口，默认请求 `http://localhost:8090/executeCode` |
| `thirdParty` | `ThirdPartyCodeSandbox` | 第三方沙箱占位实现 |

## 常用命令

```bash
# 编译并安装所有模块
mvn clean install -DskipTests

# 运行测试
mvn test

# 只编译某个服务及其依赖模块
mvn -pl fuoj-backend-question-service -am clean package -DskipTests
```

## 目录结构

```text
fuoj-backend-microservice
├── fuoj-backend-common              # 公共能力
├── fuoj-backend-model               # 公共模型
├── fuoj-backend-service-client      # Feign 客户端
├── fuoj-backend-gateway             # 网关服务
├── fuoj-backend-user-service        # 用户服务
├── fuoj-backend-question-service    # 题目服务
├── fuoj-backend-judge-service       # 判题服务
├── pom.xml                          # Maven 父工程
└── README.md
```

## 开发注意事项

- 各服务通过 Nacos 注册发现，服务名需要与 `spring.application.name` 保持一致。
- 用户登录状态存储在 Session 中，并通过 Redis 持久化，跨服务访问时需要保持 Cookie 路径为 `/api`。
- 题目提交采用异步判题，提交接口返回的是提交记录 ID，不是最终判题结果。
- 管理员权限通过 `@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)` 控制。
- 当前仓库未包含数据库建表 SQL，首次启动前需要根据 `fuoj-backend-model` 中的实体类准备表结构。
- 本地调试远程沙箱时，需要先启动兼容 `/executeCode` 接口的代码沙箱服务。
