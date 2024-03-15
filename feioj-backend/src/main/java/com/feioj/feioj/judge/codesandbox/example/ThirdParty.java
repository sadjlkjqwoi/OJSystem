package com.feioj.feioj.judge.codesandbox.example;

import com.feioj.feioj.judge.codesandbox.Codesandbox;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeRequest;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeResponse;

public class ThirdParty implements Codesandbox {
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {
        System.out.println("执行了第三方代码沙箱！！！");
        return null;
    }
}
