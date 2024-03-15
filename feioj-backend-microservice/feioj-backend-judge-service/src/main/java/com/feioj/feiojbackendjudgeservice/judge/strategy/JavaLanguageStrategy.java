package com.feioj.feiojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.feioj.feiojbackendmodel.model.codesandbox.JudgeInfo;
import com.feioj.feiojbackendmodel.model.dto.question.JudgeCase;
import com.feioj.feiojbackendmodel.model.dto.question.JudgeConfig;
import com.feioj.feiojbackendmodel.model.entity.Question;
import com.feioj.feiojbackendmodel.model.enums.JudgeInfoMessage;


import java.util.List;
import java.util.Optional;

/**
 * 判题策略
 */
public class JavaLanguageStrategy implements JudgeStrategy{
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<String> outputList=judgeContext.getOutputList();
        List<String> inputList=judgeContext.getInputList();
        List<JudgeCase> judgeCases=judgeContext.getJudgeCase();
        Question question=judgeContext.getQuestion();
        JudgeInfo judgeInfo=judgeContext.getJudgeInfo();
        JudgeInfo judgeInfoResponse=new JudgeInfo();
        Long timelimit=0L;
        Long memory=0L;
      if (judgeInfo==null||outputList==null){
          judgeInfoResponse.setMessage(JudgeInfoMessage.SYSTEM_ERROR.getValue()+"编译或运行错误");
          return judgeInfoResponse;
      }
      else {
          memory= Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
          timelimit=Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
          judgeInfoResponse.setTime(timelimit);
          judgeInfoResponse.setMemory(memory);
      }
        //1.判题结束，返回判题信息，设置题目的状态和信息
        JudgeInfoMessage judgeInfoMessage=JudgeInfoMessage.ACCEPTED;
        if (outputList.size()!=inputList.size()){
            judgeInfoMessage=JudgeInfoMessage.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }
        for (int i = 0; i < judgeCases.size() ; i++) {
              String judgeCaseOutputList= judgeCases.get(i).getOutput();
            if(!judgeCaseOutputList.equals(outputList.get(i))){
                judgeInfoMessage=JudgeInfoMessage.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
                return judgeInfoResponse;
            }
        }
        //判断题目限制
        JudgeConfig needjudgeConfig= JSONUtil.toBean(question.getJudgeConfig(),JudgeConfig.class);
        long needmemory=needjudgeConfig.getMemoryLimit();
        long needtimelimit=needjudgeConfig.getTimeLimit();

        if (memory>needmemory){
            judgeInfoMessage=JudgeInfoMessage.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }
        //java程序执行时间大概需要10s
        long java_time_host=10;
        if (timelimit-java_time_host>needtimelimit){
            judgeInfoMessage=JudgeInfoMessage.OUTPUT_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessage.getValue());
        return judgeInfoResponse;
    }
}
