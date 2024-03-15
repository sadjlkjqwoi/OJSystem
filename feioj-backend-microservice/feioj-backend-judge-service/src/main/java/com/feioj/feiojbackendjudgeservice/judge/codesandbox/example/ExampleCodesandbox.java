package com.feioj.feiojbackendjudgeservice.judge.codesandbox.example;



import com.feioj.feiojbackendjudgeservice.judge.codesandbox.Codesandbox;
import com.feioj.feiojbackendmodel.model.codesandbox.ExcuteCodeRequest;
import com.feioj.feiojbackendmodel.model.codesandbox.ExcuteCodeResponse;
import com.feioj.feiojbackendmodel.model.codesandbox.JudgeInfo;
import com.feioj.feiojbackendmodel.model.enums.QuestionSubmitStatusEnum;

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
