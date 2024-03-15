package com.feioj.feiojbackendjudgeservice.judge.codesandbox;

import com.feioj.feiojbackendmodel.model.codesandbox.ExcuteCodeRequest;
import com.feioj.feiojbackendmodel.model.codesandbox.ExcuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CodesandboxProxy implements Codesandbox{
    private Codesandbox codesandbox;
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {

        return codesandbox.excuteCode(excuteCodeRequest);
    }
}
