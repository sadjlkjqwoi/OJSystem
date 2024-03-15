package com.feioj.feiojcodesandbox;


import com.feioj.feiojcodesandbox.model.ExcuteCodeRequest;
import com.feioj.feiojcodesandbox.model.ExcuteCodeResponse;

import java.io.IOException;

/**
 * 代码沙箱的定义
 */
public interface Codesandbox {
    /**
     * 执行代码
     * @param excuteCodeRequest
     * @return
     */
    ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) throws IOException, InterruptedException;
}
