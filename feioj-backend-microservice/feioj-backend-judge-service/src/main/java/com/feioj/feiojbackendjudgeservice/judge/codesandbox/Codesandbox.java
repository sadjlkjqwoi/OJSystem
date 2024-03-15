package com.feioj.feiojbackendjudgeservice.judge.codesandbox;


import com.feioj.feiojbackendmodel.model.codesandbox.ExcuteCodeRequest;
import com.feioj.feiojbackendmodel.model.codesandbox.ExcuteCodeResponse;

/**
 * 代码沙箱的定义
 */
public interface Codesandbox {
    /**
     * 执行代码
     * @param excuteCodeRequest
     * @return
     */
    ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest);
}
