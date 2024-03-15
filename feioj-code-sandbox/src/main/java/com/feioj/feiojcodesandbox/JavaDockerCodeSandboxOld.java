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

public class JavaDockerCodeSandboxOld implements Codesandbox{
    public static void main(String[] args) throws IOException, InterruptedException {
        JavaDockerCodeSandboxOld javaNativeCodeSandbox=new JavaDockerCodeSandboxOld();
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

    private static final String GLOBAL_CODE_DIR_NAME="tmpCode";

    private  static  final  String GLBAL_JAVA_CLASS_NAME="Main.java";
    private  static final  long TIME_OUT=5000L;
    public static final Boolean FIRST_INIT=true;

    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) throws InterruptedException {
        String code = excuteCodeRequest.getCode();
        String language = excuteCodeRequest.getLanguage();
        List<String> inputList = excuteCodeRequest.getInputList();

        String userDir=System.getProperty("user.dir");
        String globalCodePathName=userDir+ File.separator+GLOBAL_CODE_DIR_NAME;
        //判断用户代码文件是否存在，没有则创建
        if (FileUtil.exist(globalCodePathName)){
            FileUtil.mkdir(globalCodePathName);
        }
        //把用户的代码隔离存放
        String userCodeParentPath=globalCodePathName+File.separator+ UUID.randomUUID();
        String userCodePath=userCodeParentPath+File.separator+GLBAL_JAVA_CLASS_NAME;
        File userCodeFile=FileUtil.writeString(code,userCodePath, StandardCharsets.UTF_8);
        System.out.println("代码文件路径为："+userCodeFile.getAbsolutePath());
        //2.编译代码，得到class文件
        String compileCmd=String.format("javac -encoding utf-8 %s",userCodeFile.getAbsolutePath());
        Process process= null;
        try {
            process = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage=ProcessUtils.ExecuteProcessAndGetMessage(process,"编译");
            System.out.println(executeMessage);
        } catch (IOException e) {
            return  getErrorResponse(e);
        }

        //3.创建容器，把文件复制到容器中
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
        hostConfig.setBinds(new Bind(userCodeParentPath,new Volume("/app")));
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
                    timeout[0] =true;
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
                executeMessageList.add(executeMessage);
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }

        }
        //4.整理返回信息
        ExcuteCodeResponse excuteCodeResponse=new ExcuteCodeResponse();
        List<String>outputList=new ArrayList<>();
        long maxTime=0;
        long maxMemory = 0;
        for (ExecuteMessage executeMessage01:executeMessageList){
            String errorMessage=executeMessage01.getErrorMessage();
            if(StrUtil.isNotBlank(errorMessage)){
                excuteCodeResponse.setMessage(errorMessage);
                //执行错误
                excuteCodeResponse.setStatus(3);
                break;
            }
            Long time=executeMessage01.getTime();
            if (time!=null){
                maxTime=Math.max(maxTime,time);
            }
            Long memory=executeMessage01.getMemory();
            if (memory!=null){
                maxMemory=Math.max(maxMemory,memory);
            }
            outputList.add(executeMessage01.getMessage());
        }
        //正常执行完成
        if (outputList.size()==executeMessageList.size()){
            excuteCodeResponse.setStatus(1);
        }
        excuteCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo=new JudgeInfo();
          judgeInfo.setMemory(maxMemory);
        judgeInfo.setTime(maxTime);
        excuteCodeResponse.setJudgeInfo(judgeInfo);

        //5.文件清理
        if (userCodeFile.getParentFile()!=null){
            boolean del=FileUtil.del(userCodeParentPath);
            System.out.println("删除"+(del?"成功":"失败"));
        }

        //删除容器
        dockerClient.removeContainerCmd(containerId).exec();
        //删除镜像
        dockerClient.removeImageCmd(image).exec();

        //查看日志
        LogContainerResultCallback logContainerResultCallback=new LogContainerResultCallback();

        dockerClient.logContainerCmd(containerId).withStdErr(true).withStdOut(true).exec(logContainerResultCallback).awaitCompletion();

        return excuteCodeResponse;
    }

    /**
     * 获取错误响应
     * @param e
     * @return
     */
    private ExcuteCodeResponse getErrorResponse(Throwable e){

        ExcuteCodeResponse excuteCodeResponse=new ExcuteCodeResponse();
        excuteCodeResponse.setOutputList(new ArrayList<>());
        excuteCodeResponse.setMessage(e.getMessage());
        excuteCodeResponse.setStatus(2);
        excuteCodeResponse.setJudgeInfo(new JudgeInfo());
        return  excuteCodeResponse;
    }
}
