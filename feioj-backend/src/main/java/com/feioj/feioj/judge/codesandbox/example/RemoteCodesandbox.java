package com.feioj.feioj.judge.codesandbox.example;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.feioj.feioj.common.ErrorCode;
import com.feioj.feioj.exception.BusinessException;
import com.feioj.feioj.judge.codesandbox.Codesandbox;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeRequest;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeResponse;
import com.github.mustachejava.Code;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.spring.web.json.Json;

public class RemoteCodesandbox implements Codesandbox {
    private static final String HTTP_REQUESR_SECRET="000";
    private static final String HTTP_REQUEST_HEADER="auth";
    @Override
    public ExcuteCodeResponse excuteCode(ExcuteCodeRequest excuteCodeRequest) {

        String url="http://localhost:8090/executeCode";
        String jsonStr=JSONUtil.toJsonStr(excuteCodeRequest);
        System.out.println("滴滴滴滴");
        String responseStr=HttpUtil.createPost(url)
                .header(HTTP_REQUEST_HEADER,HTTP_REQUESR_SECRET)
                .body(jsonStr)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"executeCode remoteCodeSand error,message:"+responseStr);
        }
        System.out.println("执行结果："+responseStr);
        return JSONUtil.toBean(responseStr,ExcuteCodeResponse.class);
    }
}
