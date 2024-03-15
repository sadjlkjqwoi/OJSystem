package com.feioj.feiojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@MapperScan("com.feioj.feiojbackendquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//@ComponentScan("com.feioj") 这个是导致重复扫包的问题
@EnableDiscoveryClient
@EnableFeignClients("com.feioj.feiojbackendserviceclient.service")
public class FeiojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeiojBackendQuestionServiceApplication.class, args);
    }

}
