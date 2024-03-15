package com.feioj.feiojcodesandbox;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.feioj.feiojcodesandbox.model.ExcuteCodeRequest;
import com.feioj.feiojcodesandbox.model.ExcuteCodeResponse;
import com.feioj.feiojcodesandbox.model.ExecuteMessage;
import com.feioj.feiojcodesandbox.model.JudgeInfo;
import com.feioj.feiojcodesandbox.utils.ProcessUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * java原生实现沙箱，直接继承
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandBoxTemplate{
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) throws InterruptedException {
        return super.excuteCode(excuteCodeRequest);
    }
}
