package com.feioj.feioj.judge.codesandbox;

import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeRequest;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeResponse;
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
