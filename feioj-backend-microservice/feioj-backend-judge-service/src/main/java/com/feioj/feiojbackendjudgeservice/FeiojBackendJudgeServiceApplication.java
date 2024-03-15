package com.feioj.feiojbackendjudgeservice;

import com.feioj.feiojbackendjudgeservice.rabbitmq.InitRabbitMq;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.feioj")
@EnableDiscoveryClient
@EnableFeignClients("com.feioj.feiojbackendserviceclient.service")
public class FeiojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        //初始化消息队列
        InitRabbitMq.doInit();
        SpringApplication.run(FeiojBackendJudgeServiceApplication.class, args);
    }

}
