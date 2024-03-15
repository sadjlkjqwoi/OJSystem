package com.feioj.feiojbackendjudgeservice.judge.codesandbox.example;


import com.feioj.feiojbackendjudgeservice.judge.codesandbox.Codesandbox;
import com.feioj.feiojbackendmodel.model.codesandbox.ExcuteCodeRequest;
import com.feioj.feiojbackendmodel.model.codesandbox.ExcuteCodeResponse;

public class ThirdParty implements Codesandbox {
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        System.out.println("执行了第三方代码沙箱！！！");
        return null;
    }
}
