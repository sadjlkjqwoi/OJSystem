package com.feioj.feioj.judge.codesandbox;

import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeRequest;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeResponse;

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
