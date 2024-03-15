package com.feioj.feiojcodesandbox.dockerDemo;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;

import java.awt.*;
import java.util.List;

public class DockerDemo {
    public static void main(String[] args) throws InterruptedException {
        //获取默认的DockerClient
        DockerClient dockerClient= DockerClientBuilder.getInstance().build();
        PingCmd pingCmd= dockerClient.pingCmd();
        pingCmd.exec();
        //拉取镜像
        String image="nginx:latest";
        PullImageCmd pullImageCmd=dockerClient.pullImageCmd(image);
//        PullImageResultCallback pullImageResultCallback=onNext(item)->{
//            System.out.println("下载镜像："+item.getStatus());
//            super.onNext(item);
//        };
//        pullImageCmd.exec(pullImageResultCallback)
//                .awaitCompletion();
//        System.out.println("下载完成");
        //创建容器
        CreateContainerCmd createContainerCmd=dockerClient.createContainerCmd(image);
        CreateContainerResponse createContainerResponse=createContainerCmd
                .withCmd("ehco","Hello Docker")
                        .exec();
        System.out.println(createContainerResponse);
        String containerId=createContainerResponse.getId();

        //查看容器的状态
        ListContainersCmd listContainersCmd=dockerClient.listContainersCmd();
        List<Container> containerList=listContainersCmd.withShowAll(true).exec();
        //启动容器
        dockerClient.startContainerCmd(containerId).exec();
        //删除容器
        dockerClient.removeContainerCmd(containerId).exec();
        //删除镜像
        dockerClient.removeImageCmd(image).exec();

        //查看日志
        LogContainerResultCallback logContainerResultCallback=new LogContainerResultCallback();

        dockerClient.logContainerCmd(containerId).withStdErr(true).withStdOut(true).exec(logContainerResultCallback).awaitCompletion();
    }
}
