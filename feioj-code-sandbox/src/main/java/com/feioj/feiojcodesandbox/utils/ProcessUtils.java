package com.feioj.feiojcodesandbox.utils;

import com.feioj.feiojcodesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 进程工具类
 */
public class ProcessUtils {
    /**
     * 进行进程并获取信息
     * @param process
     * @param opName
     * @return
     */
    public static ExecuteMessage ExecuteProcessAndGetMessage(Process process,String opName)  {
        ExecuteMessage executeMessage=new ExecuteMessage();
        int exitValue = 0;
        try {
            //计算进程的执行时间
            StopWatch stopWatch=new StopWatch();
            stopWatch.start();
            exitValue = process.waitFor();
            //正常退出
            if (exitValue==0){
                System.out.println(opName+"成功");
                executeMessage.setExitValue(exitValue);
                //分批获取进程的正常输出
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
                StringBuilder complieOutputStr= new StringBuilder();
                //逐行读取
                String compileOutPutLine;
                while ((compileOutPutLine=bufferedReader.readLine())!=null){

                    complieOutputStr.append(compileOutPutLine);
                }
                executeMessage.setMessage(complieOutputStr.toString());
                executeMessage.setExitValue(exitValue);
            }else {
                System.out.println(opName+"失败");
                //异常退出
                //分批获取进程的正常输出
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
                StringBuilder complieOutputStr= new StringBuilder();
                //逐行读取
                String compileOutPutLine;
                while ((compileOutPutLine=bufferedReader.readLine())!=null){

                    complieOutputStr.append(compileOutPutLine);
                }

                executeMessage.setMessage(complieOutputStr.toString());

                //分批获取进程的异常输出
                BufferedReader errorBufferedReader=new BufferedReader(new InputStreamReader(process.getErrorStream(),"UTF-8"));
                StringBuilder errorComplieOutputStr= new StringBuilder();
                //逐行读取
                String errorCompileOutPutLine;
                while ((errorCompileOutPutLine=errorBufferedReader.readLine())!=null){

                    errorComplieOutputStr.append(errorCompileOutPutLine);
                }

                executeMessage.setErrorMessage( errorComplieOutputStr.toString());
            }
            stopWatch.stop();
            executeMessage.setExitValue(exitValue);
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }


        return executeMessage;
    }

}
