package com.fantasy.fuojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.fantasy")
public class FuojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuojBackendJudgeServiceApplication.class, args);
    }

}
