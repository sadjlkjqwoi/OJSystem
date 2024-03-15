package com.feioj.feiojcodesandbox;

import cn.hutool.core.io.FileUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * java沙箱模版
 */
@Slf4j
public abstract class JavaCodeSandBoxTemplate  implements Codesandbox{

        private static final String GLOBAL_CODE_DIR_NAME="tmpCode";

        private  static  final  String GLBAL_JAVA_CLASS_NAME="Main.java";
        private  static final  long TIME_OUT=5000L;

    /**
     * 生成代码文件
     * @param code
     * @return
     */
    public  File saveCodeFiles(String code) {
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
            return userCodeFile;

        }

    /**
     * 2.编译文件
     * @param userCodeFile
     * @return
     */
    public  ExecuteMessage complieFile(File userCodeFile){
            String compileCmd=String.format("javac -encoding utf-8 %s",userCodeFile.getAbsolutePath());
            Process process= null;
            try {
                process = Runtime.getRuntime().exec(compileCmd);
                ExecuteMessage executeMessage= ProcessUtils.ExecuteProcessAndGetMessage(process,"编译");
                if (executeMessage.getExitValue()!=0){
                    throw new RuntimeException("编译错误");
                }
                return executeMessage;

            } catch (IOException e) {
                //return  getErrorResponse(e);
                throw new RuntimeException(e);
            }

        }

    /**
     * 3.执行文件，返回结果
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runFile(File userFile,List<String>inputList) {
            String userCodeParentPath= userFile.getParentFile().getAbsolutePath();
            List<ExecuteMessage> executeMessageList=new ArrayList<>();
            for (String inputArg:inputList){
                //3.运行代码,输出结果
                String runCmd=String.format("java -Xmx256m -Dfile.encoding=UTF-8  -cp %s Main %s",userCodeParentPath,inputArg);
                try {
                    Process  runProcess = Runtime.getRuntime().exec(runCmd);
                    //防止超时，超时即可销毁该进程
                    new Thread(()->{
                        try {
                            Thread.sleep(TIME_OUT);
                            runProcess.destroy();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                    }).start();
                    ExecuteMessage runExecuteMessage=ProcessUtils.ExecuteProcessAndGetMessage(runProcess,"运行");
                    executeMessageList.add(runExecuteMessage);
                    System.out.println(runExecuteMessage);
                } catch (IOException e) {
                    throw new RuntimeException("执行错误",e);
                    //return  getErrorResponse(e);
                }

            }
            return executeMessageList;

    }

    /**
     * 4.返回输出结果
     * @param executeMessageList
     * @return
     */
    public ExcuteCodeResponse outPutResponse(List<ExecuteMessage> executeMessageList){
        ExcuteCodeResponse excuteCodeResponse=new ExcuteCodeResponse();
        List<String>outputList=new ArrayList<>();
        long maxTime=0;
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
            outputList.add(executeMessage01.getMessage());
        }
        //正常执行完成
        if (outputList.size()==executeMessageList.size()){
            excuteCodeResponse.setStatus(1);
        }
        excuteCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo=new JudgeInfo();
//        judgeInfo.setMemory();
        judgeInfo.setTime(maxTime);
        excuteCodeResponse.setJudgeInfo(judgeInfo);
        return excuteCodeResponse;
    }

    /**
     * 5.清理文件
     * @param userCodeFile
     * @return
     */
    public Boolean clearFile(File userCodeFile){
        if (userCodeFile.getParentFile()!=null){
            boolean del=FileUtil.del(userCodeFile.getParentFile().getAbsolutePath());
            System.out.println("删除"+(del?"成功":"失败"));
            return del;
        }
        return true;
    }
    /**
     * 6.获取错误响应
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

    /**
     * 沙箱实现的流程
     * @param excuteCodeRequest
     * @return
     * @throws InterruptedException
     */
    @Override
        public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) throws InterruptedException {
            String code = excuteCodeRequest.getCode();
            List<String> inputList = excuteCodeRequest.getInputList();
            //1.获取代码文件
            File userCodeFile=saveCodeFiles(code);
            //2.编译文件
            ExecuteMessage executeCompileMessage=complieFile(userCodeFile);
            System.out.println("编译文件结果："+executeCompileMessage);
            //3.执行文件，得到输出结果
            List<ExecuteMessage> executeMessageList=runFile(userCodeFile,inputList);
            //4.整理返回信息
           ExcuteCodeResponse excuteCodeResponse=outPutResponse(executeMessageList);
            //5.文件清理
            Boolean result=clearFile(userCodeFile);
            if (!result) {
             log.error("deletef files error,userParentPath:"+userCodeFile.getParentFile().getAbsolutePath());
            }
            return excuteCodeResponse;
        }


}
