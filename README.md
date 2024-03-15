# Mars在线判题系统
## 项目介绍：
基于 Spring Boot + Spring Cloud 微服务 + Docker（+ Vue 3 + Arco Design）的编程题目在线评测系统。
在系统前台，管理员可以创建、管理题目；用户可以自由搜索题目、阅读题目、编写并提交代码。
在系统后端，能够根据管理员设定的题目测试用例在 自主实现的代码沙箱 中对代码进行编译、运行、判断输出是否正确。
其中，代码沙箱可以作为独立服务，提供给其他开发者使用。
## 文件介绍

## 技术选型
后端：
Java Spring Cloud + Spring Cloud Alibaba 微服务 ​
* Nacos 注册中心​
* OpenFeign 客户端调用​
* GateWay 网关​
  
聚合接口文档​

Java Spring Boot（万用后端模板）​

Java 进程控制​ 

Java 安全管理器​

Docker 代码沙箱实现​

虚拟机 + 远程开发​

MySQL 数据库​

MyBatis-Plus 及 MyBatis X 自动生成​

Redis 分布式 Session​

RabbitMQ 消息队列​

多种设计模式 ​
* 策略模式​
* 工厂模式​
* 代理模式​
* 模板方法模式​
* 
其他：部分并发编程、JVM 小知识

## 项目功能
#### 题目模块

添加题目（管理员
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/214a4c6c-d795-4514-97b6-7ac88dab7c18)

删除题目（管理员
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/f8033eee-eaca-48f3-934b-e8357b983688)


修改题目（管理员
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/aa925c04-445d-4943-84df-902104af58ad)


搜索题目（用户
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/cbd81322-185c-4948-9666-a4c37d567679)


浏览题目
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/846cfc4f-1a19-4219-9122-f1e643feb351)

在线做题
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/c77bd253-534f-43f7-be18-56c166ae59e6)


提交题目(查看结果
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/3145f5a0-04a5-48ff-93da-7a86dc69b5e7)




#### 用户模块

注册
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/d64a0d97-8a12-440a-b240-5d8484aed086)


登录
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/cfd9bb0f-169c-46b0-8056-89ebf68f1f71)


#### 判题模块

提交判题（结果是否正确与错误
![image](https://github.com/sadjlkjqwoi/OJSystem/assets/118719926/7f2bc6a8-eff9-4b6a-8a14-4fe2022f1c3d)

错误处理

自主实现（代码沙箱

开放接口
