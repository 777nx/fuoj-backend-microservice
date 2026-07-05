# FUOJ Backend Microservice

FUOJ Backend Microservice 是一个在线判题系统的后端微服务项目，基于 Spring Boot 与 Spring Cloud Alibaba 构建。项目按业务拆分为用户服务、题目服务、判题服务、网关服务、公共模块、模型模块和服务客户端模块，使用 Nacos 进行服务注册与发现，OpenFeign 进行服务间调用，Gateway 统一转发请求，Sentinel 提供流量治理能力，MyBatis-Plus 负责数据持久化。