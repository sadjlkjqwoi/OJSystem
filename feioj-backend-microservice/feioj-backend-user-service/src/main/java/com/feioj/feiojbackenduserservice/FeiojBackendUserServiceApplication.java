package com.feioj.feiojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

// todo 如需开启 Redis，须移除 exclude 中的内容
//如不开启redis时，则需要加上exclude = {RedisAutoConfiguration.class}
@SpringBootApplication()
@MapperScan("com.feioj.feiojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.feioj")
@EnableDiscoveryClient
@EnableFeignClients("com.feioj.feiojbackendserviceclient.service")
public class FeiojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeiojBackendUserServiceApplication.class, args);
    }

}
