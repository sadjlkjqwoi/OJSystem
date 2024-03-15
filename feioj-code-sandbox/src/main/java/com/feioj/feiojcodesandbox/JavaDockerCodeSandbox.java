package com.feioj.feiojcodesandbox;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.feioj.feiojcodesandbox.model.ExcuteCodeRequest;
import com.feioj.feiojcodesandbox.model.ExcuteCodeResponse;
import com.feioj.feiojcodesandbox.model.ExecuteMessage;
import com.feioj.feiojcodesandbox.model.JudgeInfo;
import com.feioj.feiojcodesandbox.utils.ProcessUtils;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
@Component
public class JavaDockerCodeSandbox extends JavaCodeSandBoxTemplate{
    public static void main(String[] args) throws IOException, InterruptedException {
        JavaDockerCodeSandbox javaNativeCodeSandbox=new JavaDockerCodeSandbox();
        //用户代码
        String code= ResourceUtil.readStr("testCode/simpleCompute/Main.java",StandardCharsets.UTF_8);

        ExcuteCodeRequest excuteCodeRequest=ExcuteCodeRequest
                .builder()
                .code(code)
                .inputList(Arrays.asList("2 3","4 6"))
                .language("java")
                .build();
        ExcuteCodeResponse excuteCodeResponse = javaNativeCodeSandbox.excuteCode(excuteCodeRequest);
        System.out.println(excuteCodeResponse);
    }

    private  static final  long TIME_OUT=5000L;
    public static final Boolean FIRST_INIT=true;

    //3.创建容器，把文件复制到容器中
    @Override
    public List<ExecuteMessage> runFile(File userFile, List<String> inputList) {

        //获取默认的DockerClient
        DockerClient dockerClient= DockerClientBuilder.getInstance().build();
        PingCmd pingCmd= dockerClient.pingCmd();
        pingCmd.exec();
        //拉取镜像
        String image="openjdk:8-alpine";
        if (FIRST_INIT){
            PullImageCmd pullImageCmd=dockerClient.pullImageCmd(image);
            PullImageResultCallback pullImageResultCallback=new PullImageResultCallback(){
                public void onNext(PullResponseItem item){
                    System.out.println("下载镜像："+item.getStatus());
                    super.onNext(item);
                }
            };
            try {
                pullImageCmd.exec(pullImageResultCallback)
                        .awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("拉取镜像异常");
                throw new RuntimeException(e);
            }
            System.out.println("下载完成");
        }
        //3.创建容器
        CreateContainerCmd createContainerCmd=dockerClient.createContainerCmd(image);
        HostConfig hostConfig=new HostConfig();
        hostConfig.setBinds(new Bind(userFile.getParentFile().getAbsolutePath(),new Volume("/app")));
       //限制内存、cpu数量
        hostConfig.withMemory(100*1000*1000L);
        hostConfig.withCpuCount(1L);
        CreateContainerResponse createContainerResponse=createContainerCmd
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withTty(true)
                .withCmd("ehco","Hello Docker")
                .exec();
        System.out.println(createContainerResponse);
        String containerId=createContainerResponse.getId();


        //4.启动容器
        List<ExecuteMessage> executeMessageList=new ArrayList<>();
        dockerClient.startContainerCmd(containerId).exec();
        for (String line:inputList) {
            ExecuteMessage executeMessage=new ExecuteMessage();
            StopWatch stopWatch=new StopWatch();
            final String[] errMessage = {null};
            final String[] message = {null};
            String []inputArag=line.split(",");
            String []cmdArray= ArrayUtil.append(new String[]{"java","-cp","/app","Main"},inputArag);
            ExecCreateCmdResponse excuteCodeResponse=dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStderr(true)
                    .withAttachStdout(true)
                    .withAttachStdin(true)
                    .exec();
            System.out.println("创建执行命令："+excuteCodeResponse);
            String execId=excuteCodeResponse.getId();
            final long[] memory = new long[1];
            //判断是否超时
            final Boolean[] timeout = {true};
            ExecStartResultCallback execStartResultCallback=new ExecStartResultCallback(){
                @Override
                public void onComplete() {
                    timeout[0] =false;
                    super.onComplete();

                }

                public void onNEXT(Frame frame){
                    StreamType streamType=frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)){
                        errMessage[0] =new String(frame.getPayload());
                        System.out.println("输出错误结果："+ errMessage[0]);
                    }
                    else {
                        message[0] =new String(frame.getPayload());
                        System.out.println("输出结果："+ message[0]);
                    }
                    super.onNext(frame);
                    executeMessage.setMessage(message[0]);
                    executeMessage.setErrorMessage(errMessage[0]);

                    //获取占用的内存

                    StatsCmd statsCmd=dockerClient.statsCmd(containerId);
                    ResultCallback<Statistics>statisticsResultCallback=  statsCmd.exec(new ResultCallback<Statistics>() {
                        @Override
                        public void onNext(Statistics statistics) {
                            System.out.println("内存占用："+statistics.getMemoryStats().getStats());
                            memory[0] =Math.max(statistics.getMemoryStats().getUsage(), memory[0]);
                        }

                        @Override
                        public void onStart(Closeable closeable) {

                        }


                        @Override
                        public void onError(Throwable throwable) {

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void close() throws IOException {

                        }
                    });
                    statsCmd.exec(statisticsResultCallback);
                    statsCmd.close();
                }

            };
            try {
                stopWatch.start();
                dockerClient.execStartCmd(execId).exec(execStartResultCallback).awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS);
                stopWatch.stop();
                //执行时间
                executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
                executeMessage.setMemory(memory[0]);
                executeMessage.setExitValue(0);
                executeMessageList.add(executeMessage);
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }

        }
        return executeMessageList;
    }

}
