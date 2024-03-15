package com.feioj.feioj.judge.codesandbox.example;

import com.feioj.feioj.judge.codesandbox.Codesandbox;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeRequest;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeResponse;
import com.feioj.feioj.model.dto.question_submit.JudgeInfo;
import com.feioj.feioj.model.entity.Question;
import com.feioj.feioj.model.enums.QuestionSubmitStatusEnum;

import java.util.Arrays;
import java.util.List;

public class ExampleCodesandbox implements Codesandbox {
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        //响应的输出
        List<String> inputList = Arrays.asList("1 2","3 4");
        JudgeInfo judgeInfo=new JudgeInfo();
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        judgeInfo.setMessage(QuestionSubmitStatusEnum.SUCCEED.getText());
        ExcuteCodeResponse excuteCodeResponse=ExcuteCodeResponse
                .builder()
                .outputList(inputList)
                .message("测试执行成功")
                .status(QuestionSubmitStatusEnum.SUCCEED.getValue())
                .judgeInfo(judgeInfo)
                .build();

        return excuteCodeResponse;
    }
}
