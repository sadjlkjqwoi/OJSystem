package com.feioj.feiojcodesandbox;




import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.feioj.feiojcodesandbox.model.ExcuteCodeRequest;
import com.feioj.feiojcodesandbox.model.ExcuteCodeResponse;
import com.feioj.feiojcodesandbox.model.ExecuteMessage;
import com.feioj.feiojcodesandbox.model.JudgeInfo;
import com.feioj.feiojcodesandbox.utils.ProcessUtils;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JavaNativeCodeSandboxOld implements Codesandbox{
    public static void main(String[] args) throws IOException, InterruptedException {
        JavaNativeCodeSandboxOld javaNativeCodeSandboxOld =new JavaNativeCodeSandboxOld();
        //用户代码
        String code= ResourceUtil.readStr("testCode/simpleCompute/Main.java",StandardCharsets.UTF_8);

        ExcuteCodeRequest excuteCodeRequest=ExcuteCodeRequest
                .builder()
                .code(code)
                .inputList(Arrays.asList("2 3","4 6"))
                .language("java")
                .build();
        ExcuteCodeResponse excuteCodeResponse = javaNativeCodeSandboxOld.excuteCode(excuteCodeRequest);
        System.out.println(excuteCodeResponse);
    }

    private static final String GLOBAL_CODE_DIR_NAME="tmpCode";

    private  static  final  String GLBAL_JAVA_CLASS_NAME="Main.java";
    private  static final  long TIME_OUT=5000L;
    private static final List<String> blackList=Arrays.asList("Files","exec");
    private static  final WordTree WORD_TREE ;

    static {
        //初始化字典树
        WORD_TREE=new WordTree();
        WORD_TREE.addWords(blackList);
    }
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        String code = excuteCodeRequest.getCode();
        String language = excuteCodeRequest.getLanguage();
        List<String> inputList = excuteCodeRequest.getInputList();
        //校验代码中是否含有黑名单的命令
        FoundWord foundWord=WORD_TREE.matchWord(code);
        if (foundWord!=null){
            System.out.println("包含禁止词："+foundWord.getFoundWord());
            return null;
        }
        //获取当前的工作目录
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

        List<ExecuteMessage> executeMessageList=new ArrayList<>();
        for (String inputArg:inputList){
            //3.运行代码,输出结果
            String runCmd=String.format("java Xmx256m -cp %s Main %s",userCodeParentPath,inputArg);
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
               return  getErrorResponse(e);
            }

        }
        //4.整理返回信息
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

        //5.文件清理
        if (userCodeFile.getParentFile()!=null){
            boolean del=FileUtil.del(userCodeParentPath);
            System.out.println("删除"+(del?"成功":"失败"));
        }
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
